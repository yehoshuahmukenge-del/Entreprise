package cd.esforca.entreprise.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class ImageUtils {

    /**
     * Sauvegarde une image dans le stockage interne de l'application.
     * @return Le chemin absolu du fichier enregistré ou null en cas d'erreur.
     */
    public static String saveImageToInternalStorage(Context context, Uri imageUri) {
        try {
            // 1. Ouvrir l'image depuis l'URI
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // 2. Préparer le fichier de destination (Nom unique)
            String fileName = "agent_" + UUID.randomUUID().toString() + ".jpg";
            File file = new File(context.getFilesDir(), fileName);

            // 3. Compresser et écrire l'image
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

            outputStream.flush();
            outputStream.close();
            if (inputStream != null) inputStream.close();

            return file.getAbsolutePath(); // On retourne le chemin pour la DB
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Supprime physiquement une image du stockage pour libérer de l'espace.
     */
    public static void deleteImage(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists()) file.delete();
        }
    }
}