package vsue.faults;

public class VSConfig {

    public static final int MAX_ATTEMPTS = 5;

    // -D<key>.<value> => Dvs.testcase => DPROP_KEY

    public static final int PORT = Integer.parseInt(
            System.getProperty("vs.server.port", "12345"));

    public static final int REGISTRY_PORT = Integer.parseInt(
            System.getProperty("vs.registry.port", "12677"));

    // -Dvs.testcase...
    private static final String PROPERTY_KEY = "vs.testcase";
    // VS_TESTCASE=...
    private static final String ENVIRONMENT_KEY = "VS_TESTCASE";

    public static final String TEST_CASE = computeTestCase();

    private static String computeTestCase() {
        // PROPERTY
        String value = System.getProperty(PROPERTY_KEY);
        if (value == null || value.isBlank()) {
            // ENVIRONMENT
            value = System.getenv(ENVIRONMENT_KEY);
        }
        value = (value == null || value.isBlank())
                ? "none"
                : value.trim().toLowerCase();
        if (!"none".equals(value)) {
            System.out.println("[VSServerConfig] - Test case active: " + value);
        }
        return value;
    }

    public static boolean isTest(String testCase) {
        return TEST_CASE.equalsIgnoreCase(testCase);
    }
}
