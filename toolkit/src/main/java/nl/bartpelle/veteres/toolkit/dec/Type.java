package nl.bartpelle.veteres.toolkit.dec;

/**
 * Created by Bart on 2/17/2015.
 */
public class Type {

	public static final Type INT = new Type(int.class);
	public static final Type STRING = new Type(String.class);
	public static final Type VOID = new Type(void.class);

	private Class c;

	public Type(Class c) {
		this.c = c;
	}

}
