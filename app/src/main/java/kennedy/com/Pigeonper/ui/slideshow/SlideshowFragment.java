package kennedy.com.Pigeonper.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import kennedy.com.Pigeonper.R;

public class SlideshowFragment extends Fragment {

    //private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_turnos, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        return root;
    }
}