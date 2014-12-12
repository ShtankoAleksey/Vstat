package shtanko.com.vstat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class Main extends ActionBarActivity implements OnClickListener {
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("1");
        menu.add("2");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
// TODO Call second activity
                EditText enter_name = (EditText) findViewById(R.id.enter_name);
                Intent intent = new Intent(this, ShowData.class);
                intent.putExtra("username", enter_name.getText().toString());
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
