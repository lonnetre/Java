package yoga;

import yoga.data.Pose;
import yoga.data.SessionElement;
import yoga.data.Transition;
import yoga.data.Database;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class SessionDesigner_L {

    private final Pose[] poses;
    private final Transition[] transitions;
    private boolean accelerated;

    /**
     * Intermediate results of createSession() are stored in this cache to speed up the execution.
     * The cache list has an entry for each requested duration; in it, it maps the previous pose
     * to the cached consecutive choreography
     */
    private ArrayList<Map<Pose, List<SessionElement>>> cache;

    public SessionDesigner_L(final Pose[] poses, final Transition[] transitions, boolean accelerated) {
        this.poses = poses;
        this.transitions = transitions;
        this.accelerated = accelerated;
    }

    private void destroyCache() {
        cache = null;
    }

    private void initCache(int targetDuration) {
        int cacheLength = targetDuration + 1;
        cache = new ArrayList<>(cacheLength);
        for (int i = 0; i < cacheLength; i++) {
            cache.add(new HashMap<>());
        }
    }

    public List<SessionElement> createSession(int targetDuration) {

        if (targetDuration <= 0) {
            return new LinkedList<>();
        }

        // Sometimes, doing nothing may still be better than a single pose
        int minPoseDuration = Arrays.stream(poses)
                .map(SessionElement::getDurationInSeconds)
                .reduce(Integer.MAX_VALUE, Integer::min);
        if (Math.abs(minPoseDuration - targetDuration) > targetDuration) {
            return new LinkedList<>();
        }

        if (accelerated) {
            initCache(targetDuration);
        }

        List<SessionElement> bestSolution = new LinkedList<>();
        int bestDiff = targetDuration;

        for (Pose startPose : poses) {
            List<SessionElement> tail = createSessionHelper(startPose, targetDuration - startPose.getDurationInSeconds());
            int tailDuration = SessionDesigner_L.computeDuration(tail);
            int diff = Math.abs((startPose.getDurationInSeconds() + tailDuration) - targetDuration);

            if (diff < bestDiff) {
                LinkedList<SessionElement> session = new LinkedList<>();
                session.add(startPose);
                session.addAll(tail);
                bestSolution = session;
                bestDiff = diff;

                if (bestDiff == 0) {
                    break;
                }
            }
        }

        if (accelerated) {
            destroyCache();
        }

        return bestSolution;
    }

    /**
     * Returns the yoga sequence that best continues from the current pose
     * @param currentPose the current, i.e. the latest pose in the current choreography
     * @param remainingTimeBudget how long (in seconds) the choreography to develop should take
     * @return the choreography that follows the given current pose, which comes as close as
     * possible to the remaining time budget
     */
    private List<SessionElement> createSessionHelper(final Pose currentPose, final int remainingTimeBudget) {

        if (accelerated) {
            if (remainingTimeBudget >= 0) {
                List<SessionElement> cachedTail = cache.get(remainingTimeBudget).get(currentPose);

                if (cachedTail != null) {
                    return new LinkedList<>(cachedTail);
                }
            }
        }


        if (remainingTimeBudget <= 0) {
            // From now on, everything will take too long -> stop recursion
            return new LinkedList<>();
        }

        int bestDiff = Math.abs(remainingTimeBudget);
        List<SessionElement> bestTail = new LinkedList<>();

        List<Transition> possibleTransitions = Arrays.stream(transitions)
                .filter(transition -> transition.getFrom().equals(currentPose))
                .toList();

        for (Transition transition : possibleTransitions) {
            int newRemainingTimeBudget = remainingTimeBudget - transition.getDurationInSeconds() - transition.getTo().getDurationInSeconds();

            List<SessionElement> tail = createSessionHelper(transition.getTo(), newRemainingTimeBudget);
            int tailDuration = SessionDesigner_L.computeDuration(tail);
            int diff = Math.abs(newRemainingTimeBudget - tailDuration);

            if (diff < bestDiff) {
                bestDiff = diff;

                bestTail = new LinkedList<>(tail);
                bestTail.add(0, transition.getTo());
                bestTail.add(0, transition);

                if (bestDiff == 0) {
                    break;
                }
            }
        }

        if (accelerated) {
            cache.get(remainingTimeBudget).put(currentPose, new ArrayList<>(bestTail));
        }

        return bestTail;
    }

    private static int computeDuration(List<SessionElement> choreography) {
        return choreography.stream().map(SessionElement::getDurationInSeconds).reduce(0, Integer::sum);
    }


    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.err.println("Fewer arguments than required. Start the program with: " +
                    "java -classpath yoga.jar SessionDesigner <duration> <path/to/poses> <path/to/transitions> [-accelerated]");
            System.exit(1);
        }

        // Get the parameters from the user input
        int targetDuration = Integer.parseInt(args[0]);
        Pose[] poses = Database.importYogaPoses(Path.of(args[1]));
        Transition[] transitions = Database.importYogaTransitions(Path.of(args[2]));
        boolean accelerated = args.length > 3 && args[3].equals("-accelerated");

        // Create a session with the desired duration and measure how long this takes
        long start = System.nanoTime();
        SessionDesigner_L sessionDesigner = new SessionDesigner_L(poses, transitions, accelerated);
        List<SessionElement> session = sessionDesigner.createSession(targetDuration);
        long end = System.nanoTime();

        int totalSessionDuration = 0;
        for (int i = 0; i < session.size(); i++) {
            System.out.println((i+1) + ". " + session.get(i));
            totalSessionDuration += session.get(i).getDurationInSeconds();
        }
        System.out.println("Total duration: " + totalSessionDuration + "/" + targetDuration);
        System.out.println("Computation took " + (end - start) / 1_000_000_000.0 + "s");
    }
}
