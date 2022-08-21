package dev.emortal.rayfast.area;

record ResultImpl<A, I>(I intersection, I normal, A subject, double distance) implements Intersection.Result<A, I> {
}
