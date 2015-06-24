package de.uni_freiburg.proglang.aux;

public class Pair<T,S> {

	private T left;
	private S right;
	
	public Pair(T t, S s) {
		this.left = t;
		this.right = s;
	}
	
	public T getLeft() {
		return left;
	}
	
	public S getRight() {
		return right;
	}
	
	@Override
	public String toString() {
		return "<" + left.toString() + "," + right.toString() + ">";
	}
}
