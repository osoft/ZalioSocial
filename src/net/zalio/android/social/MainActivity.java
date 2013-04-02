package net.zalio.android.social;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zalio.android.social.model.Status;
import net.zalio.android.social.model.SocialBase.OnNewStatusListener;
import net.zalio.android.social.sinaweibo.SinaWeibo;
import net.zalio.android.social.twitter.Twitter;
import net.zalio.android.social.utils.ImageBufferManager;
import net.zalio.android.social.utils.ImageBufferManager.OnThumbDownloadedListener;
import net.zalio.android.social.utils.MyLog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	ToggleButton mTglBtn;
	ListView lvList;
	ArrayList<Status> mStatusList = new ArrayList<Status>();

	private class ViewHolder{
		TextView tvText;
		TextView tvScreenName;
		TextView tvDate;
		ImageView ivThumb;
		int position;
	}
	private class StatusArrayAdapter extends ArrayAdapter<Status>{

		public StatusArrayAdapter(Context context, int textViewResourceId,
				List<Status> objects) {
			super(context, R.layout.item_layout, objects);
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent){
			View v;
			ViewHolder holder;
			if(convertView == null){
				v = View.inflate(MainActivity.this, R.layout.item_layout, null);
				holder = new ViewHolder();
				holder.tvText = (TextView)v.findViewById(R.id.tvText);
				//holder.tvText.setMovementMethod(LinkMovementMethod.getInstance());
				holder.tvScreenName = (TextView)v.findViewById(R.id.tvScreenName);
				holder.tvDate = (TextView)v.findViewById(R.id.tvDate);
				holder.ivThumb = (ImageView)v.findViewById(R.id.ivThumb);
				v.setTag(holder);
			}else{
				v = convertView;
				holder = (ViewHolder)v.getTag();
			}
			
			Status s = getItem(position);
			//Status s = mStatusList.get(position);
			//if(s == null){
			//	return v;
			//}

			holder.tvScreenName.setText(s.getUserScreenName());
			
			holder.tvText.setText(s.getText());
			//holder.tvText.setText("ASDFASDFASDFASDFASDFASDFAS asdf;aksdf;laksjdflk ASDFASDFASDF");
			//holder.tvText.setText(getRandomString());
			//holder.tvText.setText(getRandomChineseString());
			Date d = s.getDate();
			String hh = String.format("%02d", d.getHours());
			String mm = String.format("%02d", d.getMinutes());
			String ss = String.format("%02d", d.getSeconds());
			holder.tvDate.setText(hh + ":" + mm + ":" + ss);
			
			holder.position = position;
			// Set thumb image
//			new AsyncTask<Object, Void, Drawable>(){
//				private ViewHolder holder;
//				private net.zalio.android.social.model.Status s;
//
//				@Override
//				protected Drawable doInBackground(Object... params) {
//					holder = (ViewHolder)params[0];
//					s = (net.zalio.android.social.model.Status)params[1];
//					ImageBufferManager ibm = ImageBufferManager.getInstance();
//					Drawable daThumb = ibm.loadImageThumb(s.getSocialName(), 
//							String.valueOf(s.getIndex()), 
//							s.getThumbUrl());
//					return daThumb;
//				}
//				
//				@Override
//				protected void onPostExecute(Drawable d){
//					super.onPostExecute(d);
////					if(holder.position == position){
////						if(d != null){
////							MyLog.i(TAG, "Image is buffered, display!");
////							holder.ivThumb.setImageDrawable(d);
////						}else{
////							MyLog.i(TAG, "Image need reload, waiting!");
////							holder.ivThumb.setImageResource(R.drawable.ic_launcher);
////						}
////					}
//				}
//			}.execute(holder, s);
			
			if(s.getImageUrl() != null && !s.getImageUrl().equals("")){
				ImageBufferManager ibm = ImageBufferManager.getInstance();
				Drawable daThumb = ibm.loadImageThumb(s.getSocialName(), 
						String.valueOf(s.getIndex()), 
						s.getThumbUrl());
				if(daThumb != null){
					//MyLog.i(TAG, "Image is buffered, display!");
					holder.ivThumb.setImageDrawable(daThumb);
				}else{
					//MyLog.i(TAG, "Image need reload, waiting!");
					//holder.ivThumb.setImageResource(R.drawable.ic_launcher);
				}
			}else{
				holder.ivThumb.setImageDrawable(null);
			}
			
			Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
			v.setAnimation(anim);
			return v;
		}

	}	
	
	private StatusArrayAdapter statusAdapter;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				//Status s = t.getStatus(msg.arg1);
				Status s = (Status) msg.obj;
				//MyLog.i(TAG, "Adding status to list");
				if(s.getIndex() == -1 && s.getText().equals("FINISH")){
					statusAdapter.notifyDataSetChanged();
					break;
				}
				mStatusList.add(0,s);
				//int size = mStatusList.size();
				//for(int i = 0; i < size; i++){
				//	MyLog.i(TAG, mStatusList.get(i).getDate().toString());
				//}
				break;
			}
		}
	};
	final Twitter t = Twitter.getInstance();
	final SinaWeibo s = SinaWeibo.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		statusAdapter = new StatusArrayAdapter(getApplicationContext(), 0, mStatusList);
		lvList = (ListView)findViewById(R.id.listView1);
		lvList.setAdapter(statusAdapter);
		
		mTglBtn = (ToggleButton)findViewById(R.id.tglBtn);
		mTglBtn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					//t.startUpdating();
					MyLog.i(TAG, "startAuth..");
					s.startAuth(MainActivity.this);
				}else{
					//t.stopUpdating();
				}
				
			}
			
		});
		
		s.setOnNewStatusListener(new OnNewStatusListener(){

			@Override
			public void onNewStatus(Status s) {
				//mStatusList.add(s);
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = (int) s.getIndex();
				msg.obj = s;
				mHandler.sendMessage(msg);
			}
			
		});
		
		ImageBufferManager.getInstance().setThumbDownloadedLIstener(new OnThumbDownloadedListener(){

			@Override
			public void onThumbDownloaded(String socialName, String statusId) {
				//statusAdapter.notifyDataSetChanged();
				MyLog.i(TAG, "onThumbDownloaded:" + statusId);
				int count = lvList.getChildCount();
				for(int i = 0; i < count; i++){
					View v = lvList.getChildAt(i);
					ViewHolder holder = (ViewHolder)v.getTag();
					if(holder == null){
						MyLog.e(TAG, "ViewHolder is null!");
						return;
					}
					Status s = (Status) lvList.getItemAtPosition(holder.position);
					if(String.valueOf(s.getIndex()).equals(statusId)){
						MyLog.i(TAG, "Found view to update thumb");
						ImageBufferManager ibm = ImageBufferManager.getInstance();
						Drawable thumb = ibm.loadImageThumb(s.getSocialName(), String.valueOf(s.getIndex()), s.getThumbUrl());
						if(thumb != null){
							holder.ivThumb.setImageDrawable(thumb);
						}
					}
				}
			}
			
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int position,
					long id) {
				Status s = (Status)adapterView.getItemAtPosition(position);
				Intent i = new Intent(MainActivity.this, StatusDetail.class);
				i.putExtra("status", s);
				startActivity(i);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    
    private String getRandomString(){
    	int l = (int)(Math.random() * 139 + 1);
    	String s = "";
    	for(int i = 0; i < l; i++){
    		char randomLetter = (char)(Math.random ()*26+'a'); 
    		s += String.valueOf(randomLetter);
    	}
    	return s;
    }
    
    private String getRandomChineseString(){
    	String text = "今天河北省肿瘤医院，一患者跳楼自杀，原因调查中。现在的空气、水、环境严重污染加上食品不安全，导致更多人患癌，很有可能会发生在我们的亲人或自己身上！所以我呼吁从自我做起保护环境！我们有权力对破坏环境者、造假卖假者、监督执法不作为者，给与声讨与揭发！我们不要怕！我们身后站着的是正义！";
    	String s[] = {"原来这篇文章下面开吵了，好久没见吵架留言，注意文明用语，人身攻击词汇将被禁言 http://t.cn/zYdUs3A",
    			"X1的体验挺不错的了，期待升级版的X1及全新的UI //@方恨少少: 哈哈，X1的完美升级版。这个机器让我对xplay更加期待了。。。",
    			"【猴姆独家】2013年第13期美国Billboard单曲榜Top 10最终版！屌丝舞神曲Harlem Shake强势五连冠！今晚公布的最终榜单与早上的Top 10发生变动，出现乌龙事件！官方解释是之前的分数没有算好，布兰妮与P!nk位置互换，Pitbull和CA妈进入第10！虽是小变动，依然引来不少粉丝斥责……http://t.cn/zYkH0P7",
    			"以后把QQ改为电脑上的微信算了，感觉QQ完全就是电脑上的微信啊！QQ的发展和微信的发展出现了利益冲突，两者的功能都可以互相取代，比较好的是都是腾讯自家的产品，早点做好决策比较好，不要等到用户来淘汰。",
    			"我为自己分分秒秒疏漏万物向时间致歉。我为将新欢视为初恋向旧爱致歉。 我为清晨五点仍熟睡向在火车站候车的人致歉。我为桌子的四只脚向被砍下的树木致歉。 我为简短的回答向庞大的问题致歉。 ——辛波丝卡",
    			"我今天在#微盘签到#获得了37M免费空间,好运指数1颗星,你也来试试手气吧~@微盘 新浪网旗下云存储品牌，超大存储空间，海量资源下载，手机电脑数据任你同步，人手一盘，微力无限，快来体验吧！ http://t.cn/aoXFJa",
    			"转发微博",
    			"“我们用各自的方式挣扎着活下去，因为不能求死只能求生，而活着，就得把那种决心和意志显示出来。”（BY 吉本芭娜娜）晚安，各位。",
    			"谁会坚强与我并肩同行？某时某地，越过街垒，是否有个你渴求的新世界？from 《悲惨世界》",
    			"神奇的【无水洗头帽】！$5.85 免运费 免洗Shampoo帽~洗头不用水~特别适合生理期间的女生~还有生病不适合碰水洗头的童鞋们~外出旅行露营使用也很方便~ 只需戴在头上按摩头皮几分钟，然后取掉梳理头发，头发就会干净柔软如同洗过护理过一样。也可用微波炉加热15秒后再按摩~http://t.cn/zWZ2JOc",
    			"［关注叙利亚局势］秘书长潘基文3月21日表示，应叙利亚当局20日的请求，联合国将对叙利亚境内可能已使用化学武器的问题进行专业、公正和独立的调查。目前，联合国正在就调查的总体任务、调查小组的组成和包括安全因素在内的操作条件同禁止化学武器组织以及世界卫生组织进行磋商。http://t.cn/zYkHczz",
    			"站着工作对身体有好处，你们这些坐在椅子上工作的怪人。",
    			"当挖出来一大坨时，望着它，感动得想哭 //@崔东辉_:为毛女人都爱这个?",
    			"回复@赵嘉: //@赵嘉:哈哈哈哈哈哈哈哈……别人笑我太疯癫,我笑他人看不穿。入魔同学，你真以为哈苏同学只是因为被骂才不做A99的吗？……据说……几个中画幅厂家都在试图搞震撼性的产品",
    			"明天要路过上海“珠江”大桥。//@方方:  笑死了。真能恶搞呀！转给其它人笑笑。",
    			"大叔，爆张XPhone轮廓图呗~~~征用到海报去@司轩军",
    			"【猴姆独家】昔日童星JoJo热单Andre官方mv大首播！0 0 http://t.cn/zYkH7PZ @优酷网 @优酷音乐",
    			"白菜大牌！原价$149.99【Emporio Armani】太阳眼镜 $34.95 包邮 多款多色可选~ http://t.cn/zYkTenK"};
    	return s[(int)(Math.random() * s.length)];
    	//int l = text.length();
    	//return text.substring(0, (int)(Math.random() * (l - 1) + 1));
    }
}
