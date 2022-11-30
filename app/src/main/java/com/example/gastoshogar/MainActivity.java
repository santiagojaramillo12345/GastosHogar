package com.example.gastoshogar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo, jetnombre, jetgasto;
    RadioButton jrservicios, jrropa, jrcomida;

    boolean respuesta;
    String codigo, nombreP, valorGasto, tipogasto, ident_doc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar barra de titulo y asociar objetos Java con Xml
        getSupportActionBar().hide();
        jetgasto = findViewById(R.id.etgasto);
        jetnombre = findViewById(R.id.etnombreP);
        jetcodigo = findViewById(R.id.etcodigo);

        jrservicios = findViewById(R.id.rbservicios);
        jrropa = findViewById(R.id.rbropa);
        jrcomida = findViewById(R.id.rbcomida);
    }

       public void Adicionar(View view){
        codigo=jetcodigo.getText().toString();
        nombreP=jetnombre.getText().toString();
        valorGasto=jetgasto.getText().toString();
        if (codigo.isEmpty() || nombreP.isEmpty() || valorGasto.isEmpty()){
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else{
            if (jrservicios.isChecked())
                tipogasto="servicios";
            else
            if (jrropa.isChecked())
                tipogasto="ropa";
            else
                tipogasto="comida";
            // Create a new user with a first and last name
            Map<String, Object> equipo = new HashMap<>();
            equipo.put("Codigo", codigo);
            equipo.put("Nombre", nombreP);
            equipo.put("valorgasto", valorGasto);
            equipo.put("tipogasto",tipogasto);


            // Add a new document with a generated ID
            db.collection("GastosHogar")
                   .add(equipo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                       public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Documento adicionado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                 .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error al adicionar el documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void Consultar(View view){
        Buscar_equipo();
    }

    private void Buscar_equipo(){
        respuesta=false;
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "Codigo es requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }else{
            db.collection("gastosHogar")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    respuesta=true;
                                    ident_doc=document.getId();
                                    jetnombre.setText(document.getString("Nombre"));
                                    jetgasto.setText(document.getString("valorgasto"));
                                    if (document.getString("tipogasto").equals("servicios"))
                                        jrservicios.setChecked(true);
                                    else
                                    if (document.getString("tipogasto").equals("ropa"))
                                        jrropa.setChecked(true);


                                    //Log.d(TAG, document.getId() +" => " + document.getData());
                                }
                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public void Modificar(View view){
        codigo=jetcodigo.getText().toString();
        nombreP=jetnombre.getText().toString();
        valorGasto=jetgasto.getText().toString();
        if (codigo.isEmpty() || nombreP.isEmpty() || valorGasto.isEmpty()){
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else {
            if (respuesta == true) {
                if (jrservicios.isChecked())
                    tipogasto = "servicios";
                else if (jrropa.isChecked())
                    tipogasto = "ropa";
                else
                    tipogasto = "comida";
                // Create a new user with a first and last name
                Map<String, Object> equipo = new HashMap<>();
                equipo.put("Codigo", codigo);
                equipo.put("Nombre", nombreP);
                equipo.put("valorgasto", valorGasto);
                equipo.put("tipogasto", tipogasto);


                // Modify a new document with a generated ID
                db.collection("gastosHogar").document(ident_doc)
                        .set(equipo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Documento actualizado ...", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error actualizando documento...", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Debe primero consultar", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
        }
    }

    public void Anular(View view){
        codigo=jetcodigo.getText().toString();
        nombreP=jetnombre.getText().toString();
        tipogasto=jetgasto.getText().toString();
        if (codigo.isEmpty() || nombreP.isEmpty() || jetgasto.isEmpty()){
            Toast.makeText(this, "Los campos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }
        else {
            if (respuesta == true) {
                if (jrb.isChecked())
                    categoria = "Profesional";
                else if (jrbascenso.isChecked())
                    categoria = "Ascenso";
                else
                    categoria = "Aficionado";
                // Create a new user with a first and last name
                Map<String, Object> equipo = new HashMap<>();
                equipo.put("Codigo", codigo);
                equipo.put("Nombre", nombre);
                equipo.put("Ciudad", ciudad);
                equipo.put("Categoria", categoria);
                equipo.put("Activo", "no");

                // Modify a new document with a generated ID
                db.collection("Campeonato").document(ident_doc)
                        .set(equipo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Documento anulado ...", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error anulando documento...", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Debe primero consultar", Toast.LENGTH_SHORT).show();
                jetcodigo.requestFocus();
            }
        }
    }


    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetnombre.setText("");
        jetciudad.setText("");
        jrbprofesional.setChecked(true);
        jcbactivo.setChecked(false);
        jetcodigo.requestFocus();
        respuesta=false;
    }
}




