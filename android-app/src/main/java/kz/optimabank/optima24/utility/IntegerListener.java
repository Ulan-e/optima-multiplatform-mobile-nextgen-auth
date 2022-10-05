package kz.optimabank.optima24.utility;

/**
 * Created by Max on 30.03.2018.
 */

public class IntegerListener {
    public interface OnIntegerChangeListener
    {
        void onIntegerChanged(int newValue);
    }

    public static class ObservableInteger
    {
        private OnIntegerChangeListener listener;

        private int value;

        public void setOnIntegerChangeListener(OnIntegerChangeListener listener)
        {
            this.listener = listener;
        }

        public int get()
        {
            return value;
        }

        public void set(int value)
        {
            this.value = value;

            if(listener != null)
            {
                listener.onIntegerChanged(value);
            }
        }
    }
}
