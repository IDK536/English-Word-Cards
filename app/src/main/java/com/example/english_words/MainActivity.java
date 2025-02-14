package com.example.english_words;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView phraseLabel;
    private EditText userInput;
    private TextView feedback;
    private Button checkButton;
    private Map<String, String> phrases;
    private List<String> rusPhrases;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phraseLabel = findViewById(R.id.phraseLabel);
        userInput = findViewById(R.id.userInput);
        feedback = findViewById(R.id.feedback);
        checkButton = findViewById(R.id.checkButton);

        phrases = loadPhrases("text.txt");
        rusPhrases = new ArrayList<>(phrases.keySet());
        Collections.shuffle(rusPhrases);

        if (!rusPhrases.isEmpty()) {
            phraseLabel.setText(rusPhrases.get(currentIndex));
        }

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTranslation();
            }
        });
    }

    private Map<String, String> loadPhrases(String fileName) {
        Map<String, String> phraseMap = new HashMap<>();
        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" – ");
                if (parts.length == 2) {
                    phraseMap.put(parts[1].trim(), parts[0].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return phraseMap;
    }

    private void checkTranslation() {
        String userAnswer = userInput.getText().toString().trim().toLowerCase();
        String correctAnswer = phrases.get(rusPhrases.get(currentIndex)).toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            feedback.setText("Правильно!");
            feedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            feedback.setText("Неправильно. Правильный перевод: " + correctAnswer);
            feedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        currentIndex++;
        if (currentIndex < rusPhrases.size()) {
            phraseLabel.setText(rusPhrases.get(currentIndex));
            userInput.setText("");
        } else {
            phraseLabel.setText("Все фразы пройдены!");
            userInput.setEnabled(false);
            checkButton.setEnabled(false);
        }
    }
}
