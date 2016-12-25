package br.com.ezeqlabs.selltimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.onurciner.toastox.ToastOX;

import java.util.ArrayList;
import java.util.List;

import br.com.ezeqlabs.selltimer.dao.ClienteDAO;
import br.com.ezeqlabs.selltimer.dao.EnderecoDAO;
import br.com.ezeqlabs.selltimer.dao.TelefoneDAO;
import br.com.ezeqlabs.selltimer.helpers.ClienteHelper;
import br.com.ezeqlabs.selltimer.model.Cliente;
import br.com.ezeqlabs.selltimer.model.Endereco;
import br.com.ezeqlabs.selltimer.model.Telefone;
import br.com.ezeqlabs.selltimer.utils.Constantes;

public class CadastroClientesActivity extends AppCompatActivity {
    private LinearLayout llEnderecos, llTelefones, llEmails;
    private List<EditText> listaEnderecos = new ArrayList<>();
    private List<EditText> listaTelefones = new ArrayList<>();
    private List<EditText> listaEmails = new ArrayList<>();
    private ClienteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_clientes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        llEnderecos = (LinearLayout) findViewById(R.id.container_enderecos);
        llTelefones = (LinearLayout) findViewById(R.id.container_telefones);
        llEmails = (LinearLayout) findViewById(R.id.container_emails);

        helper = new ClienteHelper(CadastroClientesActivity.this, listaEnderecos, listaTelefones);
    }

    public void novoEndereco(View v){
        EditText editText = geraEditText(R.string.label_endereco);
        llEnderecos.addView(editText);
        listaEnderecos.add(editText);
    }

    public void novoTelefone(View v){
        EditText editText = geraEditText(R.string.label_telefone);
        llTelefones.addView(editText);
        listaTelefones.add(editText);
    }

    public void novoEmail(View v){
        llEmails.addView(geraEditText(R.string.label_email));
    }

    private EditText geraEditText(int hint){
        EditText editText = new EditText(this);
        editText.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHint(hint);
        return editText;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_salvar:
                salvarClienteCompleto();
                return false;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void salvarClienteCompleto(){
        Cliente cliente = helper.pegaClienteDoFormulario();
        ClienteDAO clienteDAO = new ClienteDAO(this);
        clienteDAO.close();

        Long clienteId = clienteDAO.insere(cliente);

        salvarEndereco(cliente.getEnderecos(), clienteId);
        salvarTelefones(cliente.getTelefones(), clienteId);

        ToastOX.ok(this, getString(R.string.cliente_salvo_sucesso), Toast.LENGTH_SHORT);

        Intent detalhe = new Intent(this, DetalheClienteActivity.class);
        detalhe.putExtra(Constantes.CLIENTE_INTENT, cliente);
        startActivity(detalhe);
    }

    private void salvarEndereco(List<Endereco> enderecos, Long clienteId){
        EnderecoDAO enderecoDAO = new EnderecoDAO(this);

        for(Endereco endereco : enderecos){
            enderecoDAO.insere(endereco, clienteId);
        }

        enderecoDAO.close();
    }

    private void salvarTelefones(List<Telefone> telefones, Long clienteId){
        TelefoneDAO telefoneDAO = new TelefoneDAO(this);

        for(Telefone telefone : telefones){
            telefoneDAO.insere(telefone, clienteId);
        }

        telefoneDAO.close();
    }
}
