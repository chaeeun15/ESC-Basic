package com.example.escbasicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView avatar;
    private TextView name;
    private TextView phone;
    private TextView email;
    private ImageButton prev;
    private TextView page;
    private ImageButton next;

    private int currentPage = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setUpUI();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setInfo(currentPage);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setInfo(currentPage); //정보가 바뀌었을 수 있으니 새로고침을 하라는 뜻
    }
    private void setUpUI() {
        toolbar = findViewById(R.id.contact_toolbar);
        avatar = findViewById(R.id.contact_iv_avatar);
        name = findViewById(R.id.contact_tv_name);
        phone = findViewById(R.id.contact_tv_phone);
        email = findViewById(R.id.contact_tv_email);
        prev = findViewById(R.id.contact_ibtn_prev);
        page = findViewById(R.id.contact_tv_page);
        next = findViewById(R.id.contact_ibtn_next);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                setInfo(currentPage);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                setInfo(currentPage);
            }
        });
    }

    private void setInfo(int index) {
        name.setText(DummyData.contacts.get(index).getName()); //DummyData에서 연락처 가져오기
        phone.setText(DummyData.contacts.get(index).getPhone());
        email.setText(DummyData.contacts.get(index).getEmail());

        if (index == 0) {
            prev.setVisibility(View.INVISIBLE);
        } else {
            prev.setVisibility(View.VISIBLE);
        }

        if (index == DummyData.contacts.size() - 1) {
            next.setVisibility(View.INVISIBLE);
        } else {
            next.setVisibility(View.VISIBLE);
        }

        page.setText((index + 1) + "/" + (DummyData.contacts.size())); //페이지 표시
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_contact_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText())); //전화 걸기
                startActivity(callIntent);
                break;

            case R.id.menu_contact_edit:
                Intent editIntent = new Intent(ContactActivity.this, AddEditActivity.class);
                editIntent.putExtra("add_edit", "edit");
                editIntent.putExtra("position", currentPage);
                startActivity(editIntent);
                break;

            case R.id.menu_contact_message:
                Intent messageIntent = new Intent(ContactActivity.this, MessageActivity.class);
                messageIntent.putExtra("phone_num", phone.getText().toString()); //메시지로 넘어갈 때 전화번호 값 넘어감
                startActivity(messageIntent);
                break;

            case R.id.menu_contact_delete:
                DummyData.contacts.remove(currentPage);

                if (DummyData.contacts.size() == 0) {
                    Toast.makeText(ContactActivity.this, "보여줄 연락처가 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (currentPage != 0) {
                        currentPage--;
                    }
                    setInfo(currentPage); //첫번째 페이지를 지우면 2번째 페이지의 index가 0이 되니까
                }
        }
        return super.onOptionsItemSelected(item);
    }
}