/*
 *@author:<Wallace Moura Machado de Oliveira;1110482413004>
 */
package com.example.projectheranca;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = new AlunoFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        if(item.getItemId() == R.id.menu_aluno) {
            fragment = new AlunoFragment();
        }
        else if(item.getItemId() == R.id.menu_livro) {
            fragment = new LivroFragment();
        }
        else if(item.getItemId() == R.id.menu_revista) {
            fragment = new RevistaFragment();
        }
        else if(item.getItemId() == R.id.menu_aluguel) {
            fragment = new AluguelFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}