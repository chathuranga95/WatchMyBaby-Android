package utill;

/**
 * Created by chathuranga on 3/13/2018.
 */

public class BooVariable {
//    private static BooVariable bv;
    private boolean boo = false;
    private ChangeListener listener;

//    private BooVariable(){
//        boo = false;
//    }

//    public static BooVariable getInstance(){
//        if(bv==null){
//            bv = new BooVariable();
//            return bv;
//        }
//        else {
//            return bv;
//        }
//    }

    public boolean isBoo() {
        return boo;
    }

    public void setBoo(boolean boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
