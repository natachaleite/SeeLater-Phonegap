package com.formobile.seelater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by group on 09/03/2017.
 */

public class CustomAdapterSeries extends ArrayAdapter<String> {
    Context contexto;
    String[] IDs;
    View customView;

    CustomAdapterSeries(Context context, String[] datas) {
        super(context, R.layout.default_series, datas);
        contexto = context;
        IDs = datas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        customView = inflater.inflate(R.layout.default_series, parent, false);

        final BancoController bc = new BancoController(contexto);
        final Cursor c = bc.getSerieSimples(Integer.parseInt(IDs[position]));

        final RelativeLayout r1 = (RelativeLayout)customView.findViewById(R.id.linha);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contexto,"Cliquei",Toast.LENGTH_LONG).show();

                Cursor cursor = bc.getSerieCompleto(Integer.parseInt(IDs[position]));
                String textodialogo = "Nome: "+cursor.getString(cursor.getColumnIndex(CriaBanco.NOME_SERIE))+" \n" +
                        "Produtora: "+cursor.getString(cursor.getColumnIndex(CriaBanco.NOME_PRODUTORA))+" \n" +
                        "Nº de Temporadas: "+cursor.getInt(cursor.getColumnIndex(CriaBanco.TEMPORADA))+" \n" +
                        "Gênero: "+cursor.getString(cursor.getColumnIndex(CriaBanco.GENERO))+" \n" +
                        "Comentário: "+cursor.getString(cursor.getColumnIndex(CriaBanco.COMENTARIO))+" \n" +
                        "Classificação: "+cursor.getFloat(cursor.getColumnIndex(CriaBanco.CLASSIFICACAO))+" \n";

                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                builder.setMessage(textodialogo)
                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!7
                                Intent intent = new Intent(contexto, AdicionaSeriesActivity.class);
                                intent.putExtra("tela", 1);
                                intent.putExtra("id", IDs[position]);
                                contexto.startActivity(intent);

                            }
                        })
                        .setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                bc.deletaItem(CriaBanco.SERIE, Integer.parseInt(IDs[position]));
                                ((ViewGroup)r1.getParent()).removeView(r1);
                                Toast.makeText(contexto,"Excluído com sucesso!",Toast.LENGTH_LONG).show();
                                //customView = null;
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog a = builder.create();
                a.show();
            }
        });

        TextView nomeSerie = (TextView)customView.findViewById(R.id.nomeSerie);
        TextView produtora = (TextView)customView.findViewById(R.id.produtoraSerie);

        nomeSerie.setText(c.getString(c.getColumnIndex(CriaBanco.NOME_SERIE)));
        produtora.setText(c.getString(c.getColumnIndex(CriaBanco.NOME_PRODUTORA)));

        RatingBar a = (RatingBar)customView.findViewById(R.id.ratingSerie);
        a.setNumStars(c.getInt(c.getColumnIndex(CriaBanco.CLASSIFICACAO)));

        return customView;
    }
}
