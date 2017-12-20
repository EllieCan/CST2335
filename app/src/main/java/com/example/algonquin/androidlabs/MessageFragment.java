package com.example.algonquin.androidlabs;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private TextView msgTextView;
    private TextView idTextView;
    private Button button;
    private ChatWindow chatWindow;


    public MessageFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public MessageFragment(ChatWindow chatWindow) {
        // Required empty public constructor
        this.chatWindow = chatWindow;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();

        String id = args.getString(ChatDatabaseHelper.KEY_ID);
        String msg = args.getString(ChatDatabaseHelper.KEY_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        msgTextView = getActivity().findViewById(R.id.message_textView);
        idTextView = getActivity().findViewById(R.id.id_textView);
        msgTextView.setText(msg);
        idTextView.setText(id);
        button = getActivity().findViewById(R.id.delete_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ChatDatabaseHelper.KEY_ID, idTextView.getText());
                if(chatWindow != null){
                    chatWindow.deleteMessage(idTextView.getText().toString());
                }else {
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }

            }
        });
    }

}
