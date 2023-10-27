package fr.uphf.a3ddy.controller;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import fr.uphf.a3ddy.R;

public class CreationProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_profil);

        /* Exemple pour la récupération des chips sélectionnés :

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        List<Integer> checkedChipIds = chipGroup.getCheckedChipIds();

        for (Integer chipId : checkedChipIds) {
            Chip selectedChip = findViewById(chipId);
            String chipText = selectedChip.getText().toString();

            // Traitez chaque chip sélectionné individuellement
        }

        */
    }
}
