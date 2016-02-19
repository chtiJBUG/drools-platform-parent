/**
 * 
 */
package org.chtijbug.drools.platform.rules;


public class Fibonacci {
	private final int sequence;
	private long value;

	public Fibonacci(int sequence) {
		this.sequence = sequence;
		this.value = -1;
	}

	public int getSequence() {
		return this.sequence;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "Fibonacci(" + this.sequence + "/" + this.value + ")";
	}
}
