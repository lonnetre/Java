package vsue.badbank;

import java.util.concurrent.atomic.AtomicInteger;

public class VSBankAccount {

	/* State */
	private final String owner;
	private final AtomicInteger balance;

	/**
	 * Creates a new account for the specified owner.
	 * 
	 * @param owner The account owner.
	 */
	public VSBankAccount(String owner) {
		this.owner = owner;
		this.balance = new AtomicInteger();
	}

	/**
	 * Reduces the account's balance by the specified amount, provided that
	 * there is enough money in the account. 
	 * 
	 * @param  amount The amount to withdraw.
	 * @return {@code true} if the operation was successful,
	 *         {@code false} otherwise.
	 * @throws IllegalArgumentException if the specified amount is negative.
	 */
	// public boolean withdraw(int amount) {
	// 	// Check amount
	// 	if (amount < 0) {
	// 		throw new IllegalArgumentException("One cannot withdraw a negative amount!");
	// 	}
		
	// 	// Check balance: The account balance should never be below zero!
	// 	if (balance.intValue() < amount) {
	// 		return false;
	// 	}
		
	// 	// Reduce balance atomically
	// 	balance.addAndGet(-amount);
	// 	return true;
	// }
	public boolean withdraw(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("One cannot withdraw a negative amount!");
		}
		synchronized (this) {
			if (balance.get() < amount) {
				return false;
			}
			balance.addAndGet(-amount);
			return true;
		}
	}

	/**
	 * Increases the account's balance by the specified amount.
	 * 
	 * @param  amount The amount to deposit.
	 * @throws IllegalArgumentException if the specified amount is negative.
	 */
	// public void deposit(int amount) {
	// 	// Check amount
	// 	if (amount < 0) {
	// 		throw new IllegalArgumentException("One cannot deposit a negative amount!");
	// 	}
		
	// 	// Increase balance atomically
	// 	balance.addAndGet(amount);
	// }
	public void deposit(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("One cannot deposit a negative amount!");
		}
		synchronized (this) {
			balance.addAndGet(amount);
		}
	}

	/**
	 * Transfers the specified amount from account {@code from} to account {@code to}.
	 * 
	 * @param from   The source account.
	 * @param to     The destination account.
	 * @param amount The amount to transfer.
	 * @return {@code true} if the transfer was successful,
	 *         {@code false} otherwise.
	 * @throws IllegalArgumentException if the specified amount is negative.
	 */
	// public static boolean transfer(VSBankAccount from, VSBankAccount to, int amount) {
	// 	// Check amount
	// 	if (amount < 0) {
	// 		throw new IllegalArgumentException("One cannot transfer a negative amount!");
	// 	}
		
	// 	// Perform transfer
	// 	if (from.withdraw(amount)) {
	// 		to.deposit(amount);
	// 		return true;
	// 	}
	// 	return false;
	// }
	public static boolean transfer(VSBankAccount from, VSBankAccount to, int amount) {
		if (amount < 0) throw new IllegalArgumentException("Negative Beträge nicht erlaubt");
	
		VSBankAccount first = from.owner.compareTo(to.owner) < 0 ? from : to;
		VSBankAccount second = (first == from) ? to : from;
	
		synchronized (first) {
			synchronized (second) {
				if (from.withdraw(amount)) {
					to.deposit(amount);
					return true;
				}
				return false;
			}
		}
	}	

	/**
	 * Returns this account's owner.
	 * 
	 * @return The account owner.
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Returns this account's current balance.
	 * 
	 * @return The current balance.
	 */
	public int getBalance() {
		return balance.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return owner + ": " + balance;
	}

}
