package com.example.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tuple2<T1, T2> {
	public final T1 _1;
	public final T2 _2;

	public static <X1, X2> Tuple2<X1, X2> of(X1 x1, X2 x2) {
		return new Tuple2<X1, X2>(x1, x2);
	}
}
