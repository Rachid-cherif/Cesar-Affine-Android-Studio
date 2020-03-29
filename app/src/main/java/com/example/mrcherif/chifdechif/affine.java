package com.example.mrcherif.chifdechif;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import java.lang.reflect.Array;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.icu.lang.UCharacter.toLowerCase;

public class affine extends AppCompatActivity {
    public static final int REQUEST = 40;

    TextView tt;
    Button bt1 ,b2,txt;
    EditText k1,k2;
    int var;
    String chiffr=" ",dss;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath).build() );
        setContentView(R.layout.activity_affine);
        bt1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        txt = (Button) findViewById(R.id.txt);
        k1 = (EditText) findViewById(R.id.k1);
        k2 = (EditText) findViewById(R.id.k2);
        tt = (TextView) findViewById(R.id.resu);


        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileserch();
            }
        });
        
        
        // bouton chiffré :
        bt1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {



                chiffr="";

                if (dss !=null) {
                   // dss = toLowerCase(dss);
                    if (alphbet(dss)) {
                        dss=toLowerCase(dss);
                        if ((!(k1.getText().toString().isEmpty())) && (!(k2.getText().toString().isEmpty()))) {

                            int q1 = Integer.parseInt(k1.getText().toString());
                            int q2 = Integer.parseInt(k2.getText().toString());
                            if (verif1(q1) && (verif2(q2))) {
                                for (int g = 0; g < dss.length(); g++) {


                                    var = ((stringtoint(dss.charAt(g)) * q1) + q2) % 26;

                                    chiffr = chiffr + inttostring(var);
                                }
                                enregistrer1(chiffr);
                            } else
                                Toast.makeText(affine.this, "verifier vos clés SVP", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(affine.this, " remplir le champ clé SVP", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(affine.this, "entrer votre message corecte SVP", Toast.LENGTH_SHORT).show();


                }else Toast.makeText(affine.this, "verifier votre message svp", Toast.LENGTH_SHORT).show();







            }
        });




        // bouton dechiffrer :
        b2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                chiffr="";

                if (dss !=null) {
                   // dss = toLowerCase(dss);

                    if (alphbet(dss)) {
                        dss = toLowerCase(dss);

                        if ((!(k1.getText().toString().isEmpty())) && (!(k2.getText().toString().isEmpty()))) {

                            int q1 = Integer.parseInt(k1.getText().toString());

                            int q2 = Integer.parseInt(k2.getText().toString());
                            if (verif1(q1) && (verif2(q2))) {

                                int anv=0;
                                int  flag;
                                for(int e=0;e<=26;e++){
                                    flag= (q1*e) % 26;
                                    if(flag==1)
                                        anv=e;
                                    

                                }

                                // le cas lorsque on fait (la message - clé2) / clé 1 est le resultat n'est pas entier
                                // on fait la methode de l'inverse :
                                for (int g = 0; g < dss.length(); g++) {

                                    int a = (((stringtoint(dss.charAt(g))) - q2));


                                    //  le cas ou la valeur est negative on ajoute 26
                                    if(a<0) {
                                        while (a < 0)
                                            a = a + 26;
                                        if(a*anv>25){
                                            var = a*anv % 26;
                                        } else var = a*anv ;
                                    } else var= ((a*anv) % 26);
                                    chiffr = chiffr + inttostring(var);
                                }
                                enregistrer(chiffr);
                            } else
                                Toast.makeText(affine.this, "verifier vos clés SVP", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(affine.this, " remplir le champ clé SVP", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(affine.this, "entrer votre message corecte SVP", Toast.LENGTH_SHORT).show();


                } else Toast.makeText(affine.this, "verifier votre message svp", Toast.LENGTH_SHORT).show();




            }
        });
    }

    // methode de joindre un fichier dans le reperatiore
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
                dss=lire(path);
            }
        }
    }
    private String lire(String mss) {
    	// Storage that you want to store the file text:
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
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"chiff_afine.txt");
        Toast.makeText(this, "file:  "+file, Toast.LENGTH_SHORT).show();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(chiffr.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Saved !", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "ficher no trouvé !", Toast.LENGTH_SHORT).show();

        } catch (IOException e){
            Toast.makeText(this, "erreur!", Toast.LENGTH_SHORT).show();

        }

    }

    private void enregistrer(String chiffr) {
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Dechiff_afine.txt");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(chiffr.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Saved !", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "ficher no trouvé !", Toast.LENGTH_SHORT).show();

        } catch (IOException e){
            Toast.makeText(this, "erreur!", Toast.LENGTH_SHORT).show();

        }

    }


    private boolean alphbet(String ms) {
        boolean tr=true;
        int i=0;

        while (((tr))&&(i<ms.length())){
            if(!Character.isLetter(ms.charAt(i)))
               tr=false;
            i++;
        }
        return tr;
    }

    boolean verif2(int q2) {
        boolean tr=false;
        if((q2>=0)&&(q2<=25))
            tr=true;
         return tr;
    }

    boolean verif1(int q1) {

        boolean tr=false;


            if((q1==9)||(q1==1) ||(q1==15)||(q1==3)||(q1==5)||(q1==7)||(q1==11)||(q1==21)||(q1==17)||(q1==19)||(q1==23)||(q1==25))
                tr=true;



        return tr;


    }

    int stringtoint(char c) {
        int nbr=0;
        switch (c) {

            case 'a':
                nbr= 0;break;
            case 'b':
                nbr= 1;break;
            case 'c':
                nbr= 2;break;
            case 'd':
                nbr= 3;break;
            case 'e':
                nbr= 4;break;
            case 'f':
                nbr= 5;break;
            case 'g':
                nbr= 6;break;
            case 'h':
                nbr= 7;break;
            case 'i':
                nbr= 8;break;
            case 'j':
                nbr= 9;break;
            case 'k':
                nbr= 10;break;
            case 'l':
                nbr= 11;break;
            case 'm':
                nbr= 12;break;
            case 'n':
                nbr= 13;break;
            case 'o':
                nbr= 14;break;
            case 'p':
                nbr= 15;break;
            case 'q':
                nbr= 16;break;
            case 'r':
                nbr= 17;break;
            case 's':
                nbr= 18;break;
            case 't':
                nbr= 19; break;
            case 'u':
                nbr= 20; break;
            case 'v':
                nbr= 21; break;
            case 'w':
                nbr= 22; break;
            case 'x':
                nbr= 23; break;
            case 'y':
                nbr=24; break;
            case 'z':
                nbr= 25;
        }
        return nbr;

    }

    private String inttostring(int var) {
        String nbr ="";
        switch (var) {
            
            case 0:
                nbr= "a";break;
            case 1:
                nbr= "b";break;
            case 2:
                nbr= "c";break;
            case 3:
                nbr= "d";break;
            case 4:
                nbr= "e";break;
            case 5:
                nbr= "f";break;
            case 6:
                nbr= "g";break;
            case 7:
                nbr= "h";break;
            case 8:
                nbr= "i";break;
            case 9:
                nbr= "j";break;
            case 10:
                nbr= "k";break;
            case 11:
                nbr= "l";break;
            case 12:
                nbr= "m";break;
            case 13:
                nbr= "n";break;
            case 14:
                nbr= "o";break;
            case 15:
                nbr= "p";break;
            case 16:
                nbr= "q";break;
            case 17:
                nbr= "r";break;
            case 18:
                nbr= "s";break;
            case 19:
                nbr= "t"; break;
            case 20:
                nbr= "u"; break;
            case 21:
                nbr= "v"; break;
            case 22:
                nbr= "w"; break;
            case 23:
                nbr= "x"; break;
            case 24:
                nbr= "y"; break;
            case 25:
                nbr= "z"; break;
        }
        return nbr;
        
        
        
        }
}
