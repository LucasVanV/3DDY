package fr.uphf.a3ddy.controller.fragment.posts;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.controller.activity.Accueil_fypActivity;
import fr.uphf.a3ddy.controller.fragment.FragmentAccueilFyp;
import fr.uphf.a3ddy.controller.fragment.monCompte.FragmentProfil;
import fr.uphf.a3ddy.model.Utilisateur;
import fr.uphf.a3ddy.model.posts.Post;
import fr.uphf.a3ddy.model.posts.PostRequest;
import fr.uphf.a3ddy.service.EncryptedPreferencesService;
import fr.uphf.a3ddy.service.retrofit.RetrofitService;
import fr.uphf.a3ddy.service.retrofit.api.PostApi;
import fr.uphf.a3ddy.service.retrofit.api.UserApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPoster extends Fragment {

    private static final int REQUEST_CODE = 1; //définir une valeur unique pour la demande d'upload des models
    private static final int PICK_IMAGE_REQUEST = 2; //définir une vvaleur unique pour la demande d'upload de l'image du model

    View view;
    Context context;
    private ChipGroup chipGroup;
    private TextInputLayout titre;
    private TextInputLayout description;
    private Button modifier_post;
    private TextView textViewTitre;
    Bundle bundle;
    Long postIdUpdated;
    private Uri imageUri;
    private Uri fileUri;

    private void iniUI() {
        Button bouton_ajouter_model = view.findViewById(R.id.bouton_ajouter_model_3d);
        Button bouton_photo_model = view.findViewById(R.id.bouton_ajouter_photo);
        modifier_post = view.findViewById(R.id.modifierPost);
        titre = view.findViewById(R.id.TextInputLayout_titre);
        description = view.findViewById(R.id.TextInputLayout_description);
        chipGroup = view.findViewById(R.id.chipGroup);
        textViewTitre  = view.findViewById(R.id.titre_nouvelle_publication);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.activity_poster, container, false);
        iniUI(); // récupère le xml
        button_model(view.findViewById(R.id.bouton_ajouter_model_3d)); // bouton pour les models 3D
        button_image(view.findViewById(R.id.bouton_ajouter_photo)); // bouton pour l'image du model

        // Récupérer l'id du post à partir du Bundle
        bundle = getArguments();
        if (bundle != null) {
            postIdUpdated = bundle.getLong("postId", -1L);
            // Faites quelque chose avec l'id du post ici
            Log.d("FragmentPoster", "Post ID: " + postIdUpdated);
        }

        //Permet de faire la différence entre poster ou modifier post :
        //On utilise le même layout alors si bundle (bundle = id post à modifier) == null on post sinon on modifie
        if (bundle == null) {
            Log.d("bundle", "pas de bundle");
        } else if (bundle != null) {
            Log.d("bundle", "bundle présent");
            textViewTitre.setText("Modification post");
        }

        modifier_post.setOnClickListener(view1 -> loadFragment(new FragmentPostsTemporaire()));

        chipGroup = view.findViewById(R.id.chipGroup);
        ajouterTags(); // ajoute une liste de tags au chipGroup
        return view;
    }

    //Ajouter les tags dans le chipGroup
    public void ajouterTags() {
        // Liste de tags
        List<String> tagList = Arrays.asList("Automobile", "Sport", "Objet");
        // Ajouter les chips à partir de la liste
        for (String tag : tagList) {
            Chip chip = new Chip(getContext());
            chip.setText(tag);
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }
    }

    //Fonction pour le bouton d'ajout de model
    public void button_model(Button bouton_ajouter_model) {
        // Bouton qui sert à sélectionner les modèles 3D
        bouton_ajouter_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Permet de sélectionner tous les types de fichiers
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Autorise la sélection de plusieurs fichiers
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public void button_image(Button bouton_photo_model) {
        bouton_photo_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Restreint la sélection aux images
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Récupération et traitement des fichiers selectionnés pour les models 3D
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData(); // Pour obtenir plusieurs fichiers
                if (clipData != null) {
                    int nb_model_3d = clipData.getItemCount();
                    if (nb_model_3d > 2) {
                        // Affichez un message d'erreur pour dire que seuls 20 fichiers sont autorisés.
                        LayoutInflater inflater = getLayoutInflater();
                        //On ajoute le toast personnalisé dans le layout
                        View layout = inflater.inflate(R.layout.toast_erreur_upload_models_3d,
                                (ViewGroup) view.findViewById(R.id.custom_toast_container));
                        TextView text = (TextView) layout.findViewById(R.id.text_toast_erreur_upload_models_3d);
                        text.setText("Vous ne pouvez importer que 20 modèles maximum");
                        Toast toast = new Toast(getContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    } else {
                        for (int i = 0; i < nb_model_3d; i++) {
                            Uri fileUri = clipData.getItemAt(i).getUri();
                            this.fileUri = fileUri;
                        }
                    }
                } else {
                    Uri fileUri = data.getData(); // Pour obtenir un seul fichier 3D
                    this.fileUri = fileUri;
                }
            }

        }
        //Récupération et traitement de l'image du model
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Obtenir les chips sélectionnées
            StringBuilder selectedChipsText = new StringBuilder();

            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    String chipText = chip.getText().toString();
                    selectedChipsText.append(chipText).append("/");
                }
            }

            // Supprimer le dernier "/" si nécessaire
            if (selectedChipsText.length() > 0 && selectedChipsText.charAt(selectedChipsText.length() - 1) == '/') {
                selectedChipsText.deleteCharAt(selectedChipsText.length() - 1);
            }

            String titreText = titre.getEditText().getText().toString();
            String descriptionText = description.getEditText().getText().toString();
            // L'utilisateur a choisi une image
            Uri imageUri = data.getData(); // Récupère l'URI de l'image sélectionnée
            this.imageUri = imageUri;


            // Lancer la fonction de création de profil
            Button buttonValider = view.findViewById(R.id.validateButton);
            buttonValider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO changer les commentaires pour mettre le fichier JSON

                    if (bundle == null) {
                        Log.d("bundle", "pas de bundle on poste");
                        addPost(titreText, descriptionText, "commentaires/", selectedChipsText.toString(), imageUri,
                                fileUri);
                    } else if (bundle != null) {
                        Log.d(
                                "bundle", "bundle présent on modifie le post");
                        updatePost(titreText, descriptionText, "commentaires/", selectedChipsText.toString(),
                                imageUri, fileUri);
                    }

                    Toast.makeText(context, "Publication postée !",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    public void addPost(String titre, String description, String commentaires, String textTags,
                        Uri imagePost,
                        Uri modele3d) {
        // Appel Retrofit
        //RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(context).getAuthToken
        // ());
        RetrofitService retrofitService = new RetrofitService("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGVvb29vb3YxMjNAZ21haWwuY29tIiwiaWF0IjoxNzAyODkxMzE5LCJleHAiOjE3MDI5Nzc3MTl9.R0Gtjo_hp4q2kyEr3Gfdx3wy7c9XPevTWcezIL937CY");
        PostApi postApi = retrofitService.getRetrofit().create(PostApi.class);

        try {
            // Utilisez ContentResolver pour ouvrir un InputStream à partir de l'URI
            InputStream imageInputStream = context.getContentResolver().openInputStream(imagePost);
            InputStream modele3dInputStream = context.getContentResolver().openInputStream(modele3d);

            if (imageInputStream != null && modele3dInputStream != null) {
                // Créez des fichiers temporaires distincts pour l'image et le modèle 3D
                File imageFile = createTempImageFile();
                File modele3dFile = createTempImageFile();

                if (imageFile != null && modele3dFile != null) {
                    // Copiez les données de l'InputStream vers les fichiers temporaires
                    writeInputStreamToFile(imageInputStream, imageFile);
                    writeInputStreamToFile(modele3dInputStream, modele3dFile);

                    // Créez des objets RequestBody à partir des fichiers temporaires
                    RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                    RequestBody modele3dRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), modele3dFile);

                    // Créez des MultipartBody.Part à partir des objets RequestBody
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imagePost", imageFile.getName(),
                            imageRequestBody);
                    MultipartBody.Part modele3dPart = MultipartBody.Part.createFormData("modele3d",
                            modele3dFile.getName(), modele3dRequestBody);

                    // Envoi de la demande avec l'image
                    Call<PostRequest> call = postApi.addPost(
                            RequestBody.create(MediaType.parse("text/plain"), titre),
                            RequestBody.create(MediaType.parse("text/plain"), description),
                            RequestBody.create(MediaType.parse("text/plain"), textTags),
                            RequestBody.create(MediaType.parse("text/plain"), commentaires),
                            RequestBody.create(MediaType.parse("text/plain"), textTags),
                            imagePart,
                            modele3dPart
                    );

                    // Exécutez la demande
                    requestPost(call);

                } else {
                    Toast.makeText(context, "Erreur de création du fichier temporaire", Toast.LENGTH_LONG).show();
                }

                // N'oubliez pas de fermer les InputStream lorsque vous avez terminé
                imageInputStream.close();
                modele3dInputStream.close();
            } else {
                Toast.makeText(context, "Erreur de récupération de l'image ou du modèle 3D", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("erreur", e.getLocalizedMessage());
            Toast.makeText(context, "Erreur : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void updatePost(String titre, String description, String commentaires, String textTags,
                           Uri imagePost,
                           Uri modele3d) {
        // Appel Retrofit
        //RetrofitService retrofitService = new RetrofitService(new EncryptedPreferencesService(context).getAuthToken
        // ());
        RetrofitService retrofitService = new RetrofitService("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aGVvMTIzNDU2N0BnbWFpbC5jb20iLCJpYXQiOjE3MDI1NDQ4MzgsImV4cCI6MTcwMjYzMTIzOH0.KVmhXa-gmC4EbkJqUfyySxxmo1oPM6gIXNL-SMoo9qI");
        

        PostApi postApi = retrofitService.getRetrofit().create(PostApi.class);

        try {
            // Utilisez ContentResolver pour ouvrir un InputStream à partir de l'URI
            InputStream imageInputStream = context.getContentResolver().openInputStream(imagePost);
            InputStream modele3dInputStream = context.getContentResolver().openInputStream(modele3d);

            if (imageInputStream != null && modele3dInputStream != null) {
                // Créez des fichiers temporaires distincts pour l'image et le modèle 3D
                File imageFile = createTempImageFile();
                File modele3dFile = createTempImageFile();

                if (imageFile != null && modele3dFile != null) {
                    // Copiez les données de l'InputStream vers les fichiers temporaires
                    writeInputStreamToFile(imageInputStream, imageFile);
                    writeInputStreamToFile(modele3dInputStream, modele3dFile);

                    // Créez des objets RequestBody à partir des fichiers temporaires
                    RequestBody imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                    RequestBody modele3dRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), modele3dFile);

                    // Créez des MultipartBody.Part à partir des objets RequestBody
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imagePost", imageFile.getName(),
                            imageRequestBody);
                    MultipartBody.Part modele3dPart = MultipartBody.Part.createFormData("modele3d",
                            modele3dFile.getName(), modele3dRequestBody);

                    //récupération du post à update
                    Long postId = bundle.getLong("postId", -1L);
                    Log.d("id post", String.valueOf(postIdUpdated));

                    // Envoi de la demande avec l'image
                    Call<PostRequest> call = postApi.updatePost(
                            RequestBody.create(MediaType.parse("text/plain"), titre),
                            RequestBody.create(MediaType.parse("text/plain"), description),
                            RequestBody.create(MediaType.parse("text/plain"), textTags),
                            RequestBody.create(MediaType.parse("text/plain"), commentaires),
                            RequestBody.create(MediaType.parse("text/plain"), textTags),
                            RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postIdUpdated)),
                            imagePart,
                            modele3dPart
                    );

                    requestPost(call);

                } else {
                    Toast.makeText(context, "Erreur de création du fichier temporaire", Toast.LENGTH_LONG).show();
                }

                // N'oubliez pas de fermer les InputStream lorsque vous avez terminé
                imageInputStream.close();
                modele3dInputStream.close();
            } else {
                Toast.makeText(context, "Erreur de récupération de l'image ou du modèle 3D", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("erreur", e.getLocalizedMessage());
            Toast.makeText(context, "Erreur : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void requestPost(Call call) {
        call.enqueue(new Callback<PostRequest>() {
            @Override
            public void onResponse(Call<PostRequest> call, Response<PostRequest> response) {
                if (response.isSuccessful()) {
                    PostRequest postRequest = response.body();
                    // Post réussi, redirigez l'utilisateur vers l'activité de la page d'accueil
                    Intent intentAccueil = new Intent(getContext(), Accueil_fypActivity.class);
                    startActivity(intentAccueil);
                }
            }

            @Override
            public void onFailure(Call<PostRequest> call, Throwable t) {
                Log.d("Erreur : ", t.getLocalizedMessage());
                Toast.makeText(context, "Erreur : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public File createTempImageFile() {
        try {
            // Créez un fichier temporaire dans le répertoire de cache de l'application
            File cacheDir = context.getCacheDir();
            if (cacheDir != null) {
                return File.createTempFile("temp_image", ".jpg", cacheDir);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeInputStreamToFile(InputStream inputStream, File outputFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    //Méthode pour changer de fragment
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Masquer le fragment actuel s'il y en a un
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.bloc_fragment_accueil);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        // Remplacer le fragment ou l'ajouter s'il n'y en a pas
        if (getChildFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName()) == null) {
            transaction.add(R.id.bloc_fragment_accueil, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.show(fragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }
}
