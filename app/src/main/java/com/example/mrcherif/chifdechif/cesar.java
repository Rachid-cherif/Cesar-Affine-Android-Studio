package com.example.mrcherif.chifdechif;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.icu.lang.UCharacter.toLowerCase;


public class cesar extends AppCompatActivity {
    public static final int REQUEST = 42;

    TextView tt;
    Button bt1, b2, ch;
    int var;
    String chiffr = " ", mss;
    EditText k;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_cesar);


        bt1 = (Button) findViewById(R.id.c1);
        b2 = (Button) findViewById(R.id.c2);
        ch = (Button) findViewById(R.id.sms);
        k = (EditText) findViewById(R.id.k);
        tt = (TextView) findViewById(R.id.ss);


        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileserch();
            }
        });


        // le bouton de chiffrement :
        bt1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {




                chiffr="";

                // Si le message est vide:
                if ((mss != null)) {

                    // Si le message et corecte ( les letres)
                    if (alphbet(mss)) {



                        // changer les majuscules en miniscule:
                       mss= toLowerCase(mss);
                       // Si la clé n'est pas vide:
                        if ((!(k.getText().toString().isEmpty()))) {

                            int q1 = Integer.parseInt(k.getText().toString());

                            // methode de verification de la clé:
                            if ((verif2(q1))) {
                                for (int g = 0; g < mss.length(); g++) {


                                    var = ((stringtoint(mss.charAt(g)) + q1) % 26);

                                    chiffr = chiffr + inttostring(var);

                                }


                                enregistrer1(chiffr);
                            } else
                                Toast.makeText(cesar.this, "verifier vos clés SVP", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(cesar.this, " remplir le champ clé SVP", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(cesar.this, "entrer votre message corecte SVP", Toast.LENGTH_SHORT).show();


                }else Toast.makeText(cesar.this, "verifier votre message svp", Toast.LENGTH_SHORT).show();






            }
        });


        // bouton dechifrer message :
        b2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                chiffr = "";



                    if (((mss != null))) {

                        if (alphbet(mss)) {
                            mss = toLowerCase(mss);
                            if ((!(k.getText().toString().isEmpty()))) {

                                int q1 = Integer.parseInt(k.getText().toString());

                                if ((verif2(q1))) {
                                    for (int g = 0; g < mss.length(); g++) {


                                        int a = (stringtoint(mss.charAt(g)) - q1);
                                        if(a<0) {
                                            while (a < 0)
                                                a = a + 26;

                                            var= a;

                                        } else var = a % 26;

                                        chiffr = chiffr + inttostring(var);

                                    }
                                    // nab3tho hna
                                    enregistrer(chiffr);
                                } else
                                    Toast.makeText(cesar.this, "verifier vos clés SVP", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(cesar.this, " remplir le champ clé SVP", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(cesar.this, "entrer votre message corecte SVP", Toast.LENGTH_SHORT).show();

                    } else Toast.makeText(cesar.this, "verifier votre message", Toast.LENGTH_SHORT).show();


            }
        });

    }

    // les methodes :

    private void fileserch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

         if(requestCode == REQUEST && resultCode== Activity.RESULT_OK){

        if (data != null) {
            Uri uri = data.getData();
            String path = uri.getPath();
            path = path.substring(path.indexOf(":") + 1);
            Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
             mss=lire(path);
        }
         }
    }

    private String lire(String mss) {
        File f = new File("/storage/emulated/0/"+mss);
        //File f = new File(mss);
        String bingo="";
        StringBuilder text =new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
           bingo=line;
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        return bingo;


    }

    private void enregistrer1(String chiffr) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Resultat.txt");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(chiffr.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Saved !", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "ficher no trouvé !", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "erreur!", Toast.LENGTH_SHORT).show();

        }

    }


    private boolean alphbet(String ms) {
        boolean tr = true;
        int i = 0;

        while (((tr)) && (i < ms.length())) {
            if (!Character.isLetter(ms.charAt(i)))
                tr = false;
            i++;
        }
        return tr;
    }

    boolean verif2(int q2) {
        boolean tr = false;
        if ((q2 >= 0) && (q2 <= 25))
            tr = true;
        return tr;
    }

    int stringtoint(char c) {
        int nbr = 0;
        switch (c) {

            case 'a':
                nbr = 0;
                break;
            case 'b':
                nbr = 1;
                break;
            case 'c':
                nbr = 2;
                break;
            case 'd':
                nbr = 3;
                break;
            case 'e':
                nbr = 4;
                break;
            case 'f':
                nbr = 5;
                break;
            case 'g':
                nbr = 6;
                break;
            case 'h':
                nbr = 7;
                break;
            case 'i':
                nbr = 8;
                break;
            case 'j':
                nbr = 9;
                break;
            case 'k':
                nbr = 10;
                break;
            case 'l':
                nbr = 11;
                break;
            case 'm':
                nbr = 12;
                break;
            case 'n':
                nbr = 13;
                break;
            case 'o':
                nbr = 14;
                break;
            case 'p':
                nbr = 15;
                break;
            case 'q':
                nbr = 16;
                break;
            case 'r':
                nbr = 17;
                break;
            case 's':
                nbr = 18;
                break;
            case 't':
                nbr = 19;
                break;
            case 'u':
                nbr = 20;
                break;
            case 'v':
                nbr = 21;
                break;
            case 'w':
                nbr = 22;
                break;
            case 'x':
                nbr = 23;
                break;
            case 'y':
                nbr = 24;
                break;
            case 'z':
                nbr = 25;
        }
        return nbr;

    }

    private String inttostring(int var) {
        String nbr = "";
        switch (var) {

            case 0:
                nbr = "a";
                break;
            case 1:
                nbr = "b";
                break;
            case 2:
                nbr = "c";
                break;
            case 3:
                nbr = "d";
                break;
            case 4:
                nbr = "e";
                break;
            case 5:
                nbr = "f";
                break;
            case 6:
                nbr = "g";
                break;
            case 7:
                nbr = "h";
                break;
            case 8:
                nbr = "i";
                break;
            case 9:
                nbr = "j";
                break;
            case 10:
                nbr = "k";
                break;
            case 11:
                nbr = "l";
                break;
            case 12:
                nbr = "m";
                break;
            case 13:
                nbr = "n";
                break;
            case 14:
                nbr = "o";
                break;
            case 15:
                nbr = "p";
                break;
            case 16:
                nbr = "q";
                break;
            case 17:
                nbr = "r";
                break;
            case 18:
                nbr = "s";
                break;
            case 19:
                nbr = "t";
                break;
            case 20:
                nbr = "u";
                break;
            case 21:
                nbr = "v";
                break;
            case 22:
                nbr = "w";
                break;
            case 23:
                nbr = "x";
                break;
            case 24:
                nbr = "y";
                break;
            case 25:
                nbr = "z";
        }
        return nbr;


    }

    private void enregistrer(String sms) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "dechif_cesar.txt");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(sms.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Saved !", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "ficher no trouvé !", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "erreur!", Toast.LENGTH_SHORT).show();

        }

    }




}
