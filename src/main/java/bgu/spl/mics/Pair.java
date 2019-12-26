package bgu.spl.mics;

public class Pair<T,P> {
    private T first;
    private P secound;

    public Pair(T first, P secound){
        this.first = first;
        this.secound = secound;
    }
    public T getFirst(){
        return first;
    }
    public P getSecound(){
        return secound;
    }
}
