package util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Android Developer on 8/11/2015.
 */
public class CustExpListview extends ExpandableListView {

    int intGroupPosition, intChildPosition, intGroupid;
    public List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    Context _context;



    public CustExpListview(Context context)
    {

        super(context);
        _context = context;
    }

/*    public CustExpListview(Context context,List<String> listDataHeader,
                           HashMap<String, List<String>> listChildData)
    {
          super(context);

        this._listDataHeader = listDataHeader;
        this._listDataChild  = listChildData;


    }*/

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {




        widthMeasureSpec = MeasureSpec.makeMeasureSpec(1080, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(700, MeasureSpec.AT_MOST);
      /*  widthMeasureSpec = MeasureSpec.makeMeasureSpec(476, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(700, MeasureSpec.AT_MOST);*/
    /*    widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(600, MeasureSpec.EXACTLY);*/
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
