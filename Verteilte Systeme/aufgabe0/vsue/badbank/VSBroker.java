package vsue.badbank;

public class VSBroker extends Thread {

	/* List of accounts this broker is responsible for */
	private final VSBankAccount[] accounts;

	/**
	 * Creates a new broker for the specified accounts.
	 * 
	 * @param accounts The accounts to summarize.
	 */
	public VSBroker(VSBankAccount[] accounts) {
		this.accounts = accounts;
	}

	/**
	 * Prints a report summarizing the accounts' current balances.
	 */
	private void printAccountReport() {
		int sum = 0;
		String report = "(";
		for (int i=0; i<accounts.length; i++) {
			sum += accounts[i].getBalance();
			report += accounts[i].getOwner() + ": " + format(accounts[i].getBalance());
			if (i < accounts.length - 1) report += ", ";
		}
		report += ")";
		report = "Total: " + format(sum) + " " + report;
		System.out.println(report);
	}

	/* Format helper */
	private String format(int amount) {
		return String.format("%4s", Integer.toString(amount));
	}
	
	/**
	 * This thread's loop triggering the creation of periodic reports.
	 */
	@Override
	public void run() {
		// Print account report every 100ms
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Shutting down broker, printing final report...");
				printAccountReport();
				break;
			}
			printAccountReport();
		}
	}
	
}
