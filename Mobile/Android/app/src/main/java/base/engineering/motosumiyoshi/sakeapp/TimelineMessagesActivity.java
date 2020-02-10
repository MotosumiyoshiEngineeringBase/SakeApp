package base.engineering.motosumiyoshi.sakeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import base.engineering.motosumiyoshi.sakeapp.chatkit.fixtures.MessagesFixtures;
import base.engineering.motosumiyoshi.sakeapp.chatkit.messages.MessageInput;
import base.engineering.motosumiyoshi.sakeapp.chatkit.messages.MessagesList;
import base.engineering.motosumiyoshi.sakeapp.chatkit.messages.MessagesListAdapter;
import base.engineering.motosumiyoshi.sakeapp.httpclient.OpenPNEApiWrapper;
import base.engineering.motosumiyoshi.sakeapp.chatkit.commons.models.Message;

public class TimelineMessagesActivity extends AbstractMessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener {

    public static void open(Context context) {
        context.startActivity(new Intent(context, TimelineMessagesActivity.class));
    }

    private long communityId = -1L;
    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_messages);
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);

        Intent intent = getIntent();
        communityId = intent.getLongExtra("CommunityId", -1);
        OpenPNEApiWrapper openPne = new OpenPNEApiWrapper(getApplicationContext());
        openPne.searchCommunityTimeLine(communityId, 20, this.messagesList);

        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
        input.setAttachmentsListener(this);
    }

    //メッセージ入力の送信ボタンクリック処理
    @Override
    public boolean onSubmit(CharSequence input) {
        OpenPNEApiWrapper openPne = new OpenPNEApiWrapper(getApplicationContext());
        openPne.postMessegeToCommunity(input.toString(), 1, -1, null, "community", communityId, this.messagesList);
        return true;
    }

    @Override
    public void onAddAttachments() {
        super.messagesAdapter.addToStart(
                MessagesFixtures.getImageMessage(), true);
    }

//    private void init(long communityId) {
//        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, super.imageLoader);
//        super.messagesAdapter.enableSelectionMode(this);
//        super.messagesAdapter.setLoadMoreListener(this);
//        super.messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
//                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
//                    @Override
//                    public void onMessageViewClick(View view, Message message) {
//                        AppUtils.showToast(TimelineMessagesActivity.this,
//                                message.getUser().getName() + " avatar click",
//                                false);
//                    }
//                });
//
//        this.messagesList.setAdapter(super.messagesAdapter);
//    }

    @Override
    public void onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status));
    }

    @Override
    public void onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status));
    }
}
