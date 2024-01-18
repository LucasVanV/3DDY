package fr.uphf.a3ddy.controller.fragment.model3d;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ObjParser {
    private List<Float> vertices = new ArrayList<>();
    private List<Float> textures = new ArrayList<>();
    private List<Float> normals = new ArrayList<>();
    private List<Integer> faces = new ArrayList<>();

    public void parseObjFile(InputStream filePath) throws IOException {
        InputStream file = Objects.requireNonNull(filePath);
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] tokens = line.split("\\s+");
                switch (tokens[0]) {
                    case "v":  // Vertex
                        vertices.add(Float.parseFloat(tokens[1]));
                        vertices.add(Float.parseFloat(tokens[2]));
                        vertices.add(Float.parseFloat(tokens[3]));
                        break;
                    case "vt":
                        textures.add(Float.parseFloat(tokens[1])); // coordonnée U de la texture
                        textures.add(Float.parseFloat(tokens[2])); // coordonnée V de la texture
                        break;
                    case "vn":
                        normals.add(Float.parseFloat(tokens[1])); // composante X de la normale
                        normals.add(Float.parseFloat(tokens[2])); // composante Y de la normale
                        normals.add(Float.parseFloat(tokens[3])); // composante Z de la normale
                        break;
                    case "f":
                        for (int i = 1; i < tokens.length; i++) {
                            String[] parts = tokens[i].split("/");
                            // Les indices dans un fichier .obj commencent à 1, donc on doit soustraire 1 pour obtenir l'indice correct en Java
                            faces.add(Integer.parseInt(parts[0]) - 1); // Indice du sommet
                            if (parts.length > 1 && !parts[1].isEmpty()) {
                                faces.add(Integer.parseInt(parts[1]) - 1); // Indice de la texture (si présent)
                            }
                            if (parts.length > 2) {
                                faces.add(Integer.parseInt(parts[2]) - 1); // Indice de la normale (si présent)
                            }
                        }
                }
            }
        }
    }

    // Méthodes pour obtenir les données parsées
    public List<Float> getVertices() {
        return vertices;
    }
    public List<Integer> getFaces() {
        return faces;
    }

    public void toString2() {
        String string = null;
        for(int i=0; i<vertices.size(); i++){
            string += vertices.get(i);
        }
        Log.d("Point list", "toString: vertices=" + string );

    }
}
