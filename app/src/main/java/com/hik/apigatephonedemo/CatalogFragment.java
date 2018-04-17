package com.hik.apigatephonedemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hik.apigatephonedemo.bean.ControlUnit;
import com.hik.apigatephonedemo.utils.DateUtil;
import com.hik.apigatephonedemo.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CatalogFragment extends Fragment implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2{
    private static String TAG = "CatalogFragment";
    private PullToRefreshListView lvCameraListView;
    private Button mButton,playlocalTest;
    private ControlUnitAdapter mAdapter;
    private List<ControlUnit> mDataList = new ArrayList<>();
    private  int currentPage = 0;
    private final static int PAGE_SIZE = 10;
    private ControlUnit mControlUnit;
    private MsgHandler mMsgHandler = new MsgHandler(this);
    private final static int UI_MSG_LISTVIEW_LOAD = 0;
    private final static int UI_MSG_LISTVIEW_LOAD_NO_MORE = 4;
    private final static int UI_MSG_LISTVIEW_LOAD_FAIL = 5;
    private final static int UI_MSG_LISTVIEW_UPDATE_CONTROLUNIT = 1;
    private final static int UI_MSG_LISTVIEW_GET_HLS = 2;
    private final static int UI_MSG_LISTVIEW_GET_RTSP = 3;
    private final String STRPOSITION = "POSITION";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        lvCameraListView = (PullToRefreshListView) view.findViewById(R.id.lvCameraListView);
        mAdapter = new ControlUnitAdapter(getContext(), mDataList);
        lvCameraListView.setAdapter(mAdapter);
        mButton = (Button)view.findViewById(R.id.refreshBtn);
        playlocalTest = (Button) view.findViewById(R.id.playlocalTest);
        lvCameraListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initData() {
        load(String.valueOf(0), String.valueOf(PAGE_SIZE), mControlUnit);
    }

    private void initListener() {
        playlocalTest.setOnClickListener(this);
        lvCameraListView.setOnRefreshListener(this);
        mButton.setOnClickListener(this);
        lvCameraListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ControlUnit controlUnit = mDataList.get(position);
                if (null == controlUnit || controlUnit.getIndexCode().equals("0")){
                    return;
                }
                getRtsp(controlUnit.getCameraInfo().getIndexCode());
            }
        });
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    private void foldListView(String strIndexCode){
        if (null == strIndexCode || "".equals(strIndexCode)){
            return;
        }

        deleteChild(strIndexCode);
        mAdapter.setList(mDataList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 删除子节点
     * @param strIndexCode
     */
    private void deleteChild(String strIndexCode){
        int i=0;
        while(i < mDataList.size()){
            if (strIndexCode.equals(mDataList.get(i).getParentIndexCode())){
                if (hasChild(mDataList.get(i).getIndexCode())){
                    deleteChild(mDataList.get(i).getIndexCode());
                }else {
                    mDataList.remove(i);
                }
            }else {
                i++;
            }
        }
    }

    private boolean hasChild(String strIndexCode){
        for (int i = 0; i<mDataList.size(); i++){
            if (strIndexCode.equals(mDataList.get(i).getParentIndexCode())){
                return true;
            }
        }

        return false;
    }

    private void load(final String start){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDataList = ApiGate.getInstance().findControlUnitByUnitCode(start);

                if (mDataList != null){
                    sendMessage(UI_MSG_LISTVIEW_LOAD, null, mDataList);
                }
            }
        }).start();
    }

    private void load(final String start,final String size,final ControlUnit mControlUnit){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDataList = ApiGate.getInstance().findCameraListByUser(start,size,mControlUnit);

                if (mDataList != null){
                    sendMessage(UI_MSG_LISTVIEW_LOAD, null, mDataList);
                }
            }
        }).start();
    }

    private void loadMore(final String start,final String size,final ControlUnit mControlUnit){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ControlUnit> list = ApiGate.getInstance().findCameraListByUser(start, size, mControlUnit);
                if (list.size() == 0) {
                    sendMessage(UI_MSG_LISTVIEW_LOAD_NO_MORE, null, mDataList);
                } else if (list.size() > 0) {
                    mDataList.addAll(ApiGate.getInstance().findCameraListByUser(start,size,mControlUnit));
                    sendMessage(UI_MSG_LISTVIEW_LOAD, null, mDataList);
                } else if (list == null) {
                    sendMessage(UI_MSG_LISTVIEW_LOAD_FAIL, null, mDataList);
                }
            }
        }).start();
    }

    private void load(final String strUnitCode, final int nPosition){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ControlUnit> list = ApiGate.getInstance().findControlUnitByUnitCode(strUnitCode);

                if (list != null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(STRPOSITION, nPosition);
                    sendMessage(UI_MSG_LISTVIEW_UPDATE_CONTROLUNIT, bundle, list);
                }
            }
        }).start();
    }

    /**
     * 用于加载监控点信息，点击区域之后
     * @param strUnitCode
     * @param nPosition
     * @param parentControlUnit
     */
    private void load(final String strUnitCode, final int nPosition, final ControlUnit parentControlUnit){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ControlUnit> list = ApiGate.getInstance().findCameraInfoPageByTreeNode(strUnitCode, parentControlUnit);

                if (list != null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(STRPOSITION, nPosition);
                    sendMessage(UI_MSG_LISTVIEW_UPDATE_CONTROLUNIT, bundle, list);
                }
            }
        }).start();
    }

    /**
     *  获取HLS地址
     * @param strIndexCode
     */
    private void getHls(final String strIndexCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strResult = ApiGate.getInstance().getHLSByIndexCode(strIndexCode);

                if (strResult != null && !"".equals(strResult)){
                    sendMessage(UI_MSG_LISTVIEW_GET_HLS, null, strResult);
                }
            }
        }).start();
    }

    /**
     *  获取RTSP地址
     * @param indexcode
     */
    private void getRtsp(final String indexcode){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String strResult = ApiGate.getInstance().getRTSPByIndexCode(strIndexCode);
                String strResult = ApiGate.getInstance().findPreviewUrlByUserAndCamera(indexcode);
                Bundle bundle=new Bundle();
                bundle.putString(MainActivity.INDEXCODE,indexcode);
                if (strResult != null && !"".equals(strResult)){
                    sendMessage(UI_MSG_LISTVIEW_GET_RTSP, bundle, strResult);
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refreshBtn:
                load("0","1000",mControlUnit);
//                load("0");
                break;
            case R.id.playlocalTest:
                startActivity(new Intent(getActivity(), LocalRecPlay.class));
                break;
            default:
                break;
        }
    }

    public void handleNotifyDataSetChanged(){
        mAdapter.setList(mDataList);
        mAdapter.notifyDataSetChanged();
        lvCameraListView.onRefreshComplete();
//        mListView.setSelection(0);
    }

    public void handleNotifyDataSetChanged(int nPosition, List<ControlUnit> list){
        if (null == list || nPosition < 0 || nPosition >= mDataList.size()){
            return;
        }

        mDataList.addAll(nPosition+1, list);
        mAdapter.setList(mDataList);
        mAdapter.notifyDataSetChanged();
//        mListView.setSelection(0);
    }

    public void handleGetHls(String strHLS){
        if (null == strHLS || "".equals(strHLS)){
            Toast.makeText(getActivity(), "获取HLS失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strHLS.contains("http")){
            Toast.makeText(getActivity(), "获取HLS成功", Toast.LENGTH_SHORT).show();

            if (getActivity() instanceof  MainActivity){
                Intent intent = new Intent(getActivity(), AudioVideoActivity.class);
                intent.putExtra("URL", strHLS);
                startActivity(intent);

            }else if (getActivity() instanceof AudioVideoActivity){
                ((AudioVideoActivity)getActivity()).initPlayer(strHLS);
            }

        }else {
            Toast.makeText(getActivity(), strHLS, Toast.LENGTH_SHORT).show();
        }
    }

    public void handleGetRtsp(String strRtsp,String indexcode){
        if (null == strRtsp || "".equals(strRtsp)){
            Toast.makeText(getActivity(), "获取RTSP失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strRtsp.contains("rtsp")){
            Toast.makeText(getActivity(), "获取RTSP成功", Toast.LENGTH_SHORT).show();

            if (getActivity() instanceof  MainActivity){
                Intent intent = new Intent(getActivity(), AudioVideoActivity.class);
                intent.putExtra("URL", strRtsp);
                intent.putExtra(MainActivity.INDEXCODE, indexcode);
                intent.putExtra("showPTZ", true);
                startActivity(intent);

            }else if (getActivity() instanceof AudioVideoActivity){
                ((AudioVideoActivity)getActivity()).initPlayer(strRtsp);
            }

        }else {
            Toast.makeText(getActivity(), strRtsp, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(int caseId, Bundle data, Object object)
    {
        if (mMsgHandler == null)
        {
            Log.e(TAG, "sentMessage,param error,msgHandler is null");
            return;
        }
        Message msg = Message.obtain();
        msg.what = caseId;
        if (data != null)
        {
            msg.setData(data);
        }
        if (object != null){
            msg.obj = object;
        }
        mMsgHandler.sendMessage(msg);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        ILoadingLayout startLabels = lvCameraListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("更新于"+ DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));// 刷新时
        startLabels.setReleaseLabel("释放刷新...");// 下来达到一定距离时，显示的提示
        load(String.valueOf(0), String.valueOf(PAGE_SIZE),mControlUnit);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        ILoadingLayout endLabels = lvCameraListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("更新于"+ DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));// 刷新时
        endLabels.setReleaseLabel("释放加载更多...");// 下来达到一定距离时，显示的提示
        currentPage++;
        loadMore(String.valueOf(currentPage), String.valueOf(PAGE_SIZE),mControlUnit);
    }

    class MsgHandler extends Handler {
        WeakReference<CatalogFragment> mFragment;

        MsgHandler(CatalogFragment fragment) {
            if (fragment == null) {
                return;
            }
            mFragment = new WeakReference<CatalogFragment>(fragment);
        }

        public void handleMessage(Message msg) {
            if (mFragment == null) {
                return;
            }

            switch (msg.what){
                case UI_MSG_LISTVIEW_LOAD:
                {
                    mFragment.get().handleNotifyDataSetChanged();
                    break;
                }
                case UI_MSG_LISTVIEW_LOAD_FAIL:
                    ToastUtil.showToast(getActivity(), "获取监控点列表失败");
                    lvCameraListView.onRefreshComplete();
                    break;
                case UI_MSG_LISTVIEW_LOAD_NO_MORE:
                    currentPage--;
                    ToastUtil.showToast(getActivity(), "已加载全部");
                    lvCameraListView.onRefreshComplete();
                    break;
                case UI_MSG_LISTVIEW_UPDATE_CONTROLUNIT:
                {
                    if (msg.obj != null){
                        Bundle bundle = msg.getData();
                        int nPosition = -1;
                        if (null != bundle){
                            nPosition = bundle.getInt(STRPOSITION);
                        }
                        List<ControlUnit> list = (List<ControlUnit>)msg.obj;
                        mFragment.get().handleNotifyDataSetChanged(nPosition, list);
                    }

                    break;
                }
                case  UI_MSG_LISTVIEW_GET_HLS:
                {
                    if (msg.obj != null){
                        String strHLS = (String)msg.obj;
                        mFragment.get().handleGetHls(strHLS);
                    }

                    break;
                }
                case UI_MSG_LISTVIEW_GET_RTSP:
                {
                    Bundle data = msg.getData();
                    String indexcode = data.getString(MainActivity.INDEXCODE);
                    if (!TextUtils.isEmpty(indexcode) && msg.obj != null) {
                        String strUrl = (String)msg.obj;
                        mFragment.get().handleGetRtsp(strUrl,indexcode);
                    }
                    break;
                }

            }
        }
    }

}
