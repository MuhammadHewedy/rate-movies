package com.example.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tuple3<T1, T2, T3> {
	public final T1 _1;
	public final T2 _2;
	public final T3 _3;

	public static <X1, X2, X3> Tuple3<X1, X2, X3> of(X1 x1, X2 x2, X3 x3) {
		return new Tuple3<X1, X2, X3>(x1, x2, x3);
	}
}
