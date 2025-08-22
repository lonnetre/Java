package vsue.badbank;

public class VSBankTest {

	private static final int NUMTHREADS = 10;
	private static final int NUMTRANSFERS = 100000;
	private static final int NUMACCOUNTS = 10;
	private static final int STARTAMOUNT = 100;

	private static void simpleTest() throws InterruptedException {
		// Creating accounts
		VSBankAccount[] accounts = createAccounts();
		// Moving money between accounts (transfer and withdraw/deposit)
		Thread[] threads = movingMoney(accounts, false);
		for (Thread t : threads) {
			t.join();
		}
		// End result of all account balance
		System.out.println("Total sum of all accounts at the end: ");
		System.out.println();
		int sum = 0;
		for (int i=0; i < 10; i++) {
			System.out.println("Account " + accounts[i]);
			sum += accounts[i].getBalance();
		}
		System.out.println("--------------");
		System.out.println("Sum:       " + sum);
	}

	private static void fancyTest() throws InterruptedException {
		// Creating bank accounts
		VSBankAccount[] accounts = createAccounts();
		// Starting broker monitoring account balances
		VSBroker broker = new VSBroker(accounts);
		broker.start();
		// Moving money (transfer only)
		Thread[] threads = movingMoney(accounts, true);
		for (Thread t : threads) {
			t.join();
		}
		broker.interrupt();
	}

	private static VSBankAccount[] createAccounts() {
		VSBankAccount[] accounts = new VSBankAccount[NUMACCOUNTS];
		System.out.println("Creating " +  NUMACCOUNTS + " accounts with balance " + STARTAMOUNT + " each.");
		for (int i=0; i < 10; i++) {
			accounts[i] = new VSBankAccount(String.valueOf(i));
			accounts[i].deposit(STARTAMOUNT);
		}
		return accounts;
	}
	
	private static Thread[] movingMoney(VSBankAccount[] accounts, boolean onlyTransfer) {
		Thread[] threads = new Thread[NUMTHREADS];
		System.out.println("Moving money between accounts....");
		for (int i=0; i < NUMTHREADS; i++) {
			threads[i] = new Thread(() -> {
				for (int j=0; j<NUMTRANSFERS; j++) {
					// Transfer random amount
					int amount = (int) (Math.random() * STARTAMOUNT);
					int fromId = (int) (Math.random() * NUMACCOUNTS);
					int toId;
					do {
						toId = (int) (Math.random() * NUMACCOUNTS);
					} while (fromId == toId);
					
					try {
						// Transferring money automatically in one direction...
						VSBankAccount.transfer(accounts[fromId], accounts[toId], amount);

						if (!onlyTransfer) {
							// ...and possibly manually the other (testing withdraw and deposit)
							if (accounts[toId].withdraw(amount)) {
								accounts[fromId].deposit(amount);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					int balanceFrom = accounts[fromId].getBalance();
					int balanceTo = accounts[toId].getBalance();
					if (balanceFrom < 0 || balanceTo < 0) {
						System.err.println("Account balance should never be below zero!\n" +
											accounts[fromId].getOwner() + ": " + balanceFrom + ", " +
											accounts[toId].getOwner() + ": " + balanceTo);
						System.exit(1);
					}
				}
			});
			threads[i].start();
		}
		return threads;
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("usage: java " + VSBankTest.class.getCanonicalName() + " [simple|fancy]");
			System.exit(1);
		}

		if (args[0].equals("simple")) {
			simpleTest();
		} else if (args[0].equals("fancy")) {
			fancyTest();
		} else {
			System.err.println("usage: java " + VSBankTest.class.getCanonicalName() + " [simple|fancy]");
			System.exit(1);
		}
	}
}
