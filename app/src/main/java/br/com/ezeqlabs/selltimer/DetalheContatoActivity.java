package br.com.ezeqlabs.selltimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onurciner.toastox.ToastOX;

import br.com.ezeqlabs.selltimer.database.DatabaseHelper;
import br.com.ezeqlabs.selltimer.model.Cliente;
import br.com.ezeqlabs.selltimer.model.Contato;
import br.com.ezeqlabs.selltimer.utils.Constantes;

public class DetalheContatoActivity extends AppCompatActivity {
    private Cliente cliente;
    private Contato contato;
    private TextView tituloCliente, dataInteresse, anotacoesContato;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_contato);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preparaVariaveis();
        preparaTextos();
        preparaPublicidade();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_editar:
                abreEdicao();
                return true;
            case R.id.action_deletar:
                exibeAlertDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void preparaVariaveis(){
        cliente = (Cliente) getIntent().getSerializableExtra(Constantes.CLIENTE_INTENT);
        contato = (Contato) getIntent().getSerializableExtra(Constantes.CONTATO_INTENT);

        tituloCliente = (TextView) findViewById(R.id.titulo_cliente_contato);
        dataInteresse = (TextView) findViewById(R.id.data_interesse_contato);
        anotacoesContato = (TextView) findViewById(R.id.anotacoes_contato);
        mAdView = (AdView) findViewById(R.id.adViewContato);
    }

    private void preparaTextos(){
        String textoDataInteresse = contato.getData() + " - " + contato.getInteresse();

        tituloCliente.setText(cliente.getNome());
        dataInteresse.setText(textoDataInteresse);
        anotacoesContato.setText(contato.getAnotacoes());
    }

    private void preparaPublicidade(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void abreEdicao(){
        Intent edicao = new Intent(this, CadastroContatoActivity.class);
        edicao.putExtra(Constantes.CONTATO_INTENT, contato);
        edicao.putExtra(Constantes.CLIENTE_INTENT, cliente);
        startActivity(edicao);
        finish();
    }

    private void exibeAlertDelete(){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning)
                .setTitle(getString(R.string.title_alert_deleta_contato))
                .setMessage(getString(R.string.texto_alert_deleta_contato))
                .setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletaContato();
                    }
                })
                .setNegativeButton(getString(R.string.nao), null)
                .show();
    }

    private void deletaContato(){
        databaseHelper.deletaContatoCliente(contato);
        ToastOX.ok(this, getString(R.string.contato_deletado_sucesso), Toast.LENGTH_LONG);
        finish();
    }
}
