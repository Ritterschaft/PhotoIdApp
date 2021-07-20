package com.ruoqian.brainidphoto.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.longtu.base.util.StringUtils;
import com.ruoqian.lib.bean.IdphotoBean;
import com.ruoqian.lib.bean.IdphotoColorBean;
import com.ruoqian.lib.bean.IdphotoPrintBean;
import com.ruoqian.lib.utils.DateUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoManager {

    private static final String DBNAME = "ruoqian_idphoto.db";

    private Context context;
    private static DaoManager daoManager;

    private DBOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private IdphotoDao idphotoDao;
    private IdphotoPrintDao idphotoPrintDao;
    private IdphotoColorDao idphotoColorDao;
    private SearchRecordDao searchRecordDao;


    public DaoManager(Context context) {
        this.context = context;
        mHelper = new DBOpenHelper(context, DBNAME, null);
        mDaoMaster = new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    private SQLiteDatabase getWritableDatabase() {
        if (mHelper == null) {
            mHelper = new DBOpenHelper(context, DBNAME, null);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (mHelper == null) {
            mHelper = new DBOpenHelper(context, DBNAME, null);
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db;
    }

    public static DaoManager getInstance(Context context) {
        if (daoManager == null) {
            synchronized (DaoManager.class) {
                if (daoManager == null) {
                    daoManager = new DaoManager(context);
                }
            }
        }
        return daoManager;
    }

    private void isIdphotoDao() {
        if (idphotoDao == null) {
            idphotoDao = mDaoSession.getIdphotoDao();
        }
    }

    private void isIdphotoPrintDao() {
        if (idphotoPrintDao == null) {
            idphotoPrintDao = mDaoSession.getIdphotoPrintDao();
        }
    }

    private void isIdphotoColorDao() {
        if (idphotoColorDao == null) {
            idphotoColorDao = mDaoSession.getIdphotoColorDao();
        }
    }

    private void isSearchRecordDao() {
        if (searchRecordDao == null) {
            searchRecordDao = mDaoSession.getSearchRecordDao();
        }
    }

    /**
     * 获取证件照列表
     */
    public List<Idphoto> getIdphotoLists(boolean isRecommend, String keyWord) {
        isIdphotoDao();
        if (isRecommend) {
            return idphotoDao.queryBuilder()
                    .where(IdphotoDao.Properties.RecommendTime.gt(0))
                    .orderDesc(IdphotoDao.Properties.RecommendTime)
                    .list();
        }
        if (StringUtils.isEmpty(keyWord)) {
            return idphotoDao.queryBuilder()
                    .orderDesc(IdphotoDao.Properties.RecommendTime)
                    .list();
        } else {
            return idphotoDao.queryBuilder()
                    .where(IdphotoDao.Properties.Name.like("%" + keyWord +"%"))
                    .orderDesc(IdphotoDao.Properties.RecommendTime)
                    .list();
        }
    }

    /**
     * 设置证件照数据
     */
    public void setIdphotoData(List<IdphotoBean> listIdphotos) {
        if (listIdphotos == null || listIdphotos.size() == 0) {
            return;
        }
        List<Idphoto> idphotos = getIdphotoLists(false, null);
        if (idphotos == null) {
            idphotos = new ArrayList<>();
        }
        int len = idphotos.size();
        int size = listIdphotos.size();
        for (int i = 0; i < size; i++) {
            int j = 0;
            for (; j < len; j++) {
                if (idphotos.get(j).getCloudId() == listIdphotos.get(i).getId()) { //有对比更新
                    if (idphotos.get(j).getCloudUpdateTime() < DateUtils.dateToStamp(listIdphotos.get(i).getUpdateTime())) {
                        //更新数据
                        updateIdphoto(listIdphotos.get(i), idphotos.get(j));
                    }
                    idphotos.remove(j);
                    j--;
                    len--;
                    break;
                }
            }
            if (j == len) { //没有添加
                addIdphoto(listIdphotos.get(i));
            }
        }

        if (idphotos.size() > 0) {
            len = idphotos.size();
            List<Long> listIds = new ArrayList<>();
            for (int j = 0; j < len; j++) {
                listIds.add(idphotos.get(j).getId());
            }
            delIdphoto(listIds);
        }
    }

    /**
     * 删除证件照
     */
    public void delIdphoto(List<Long> listIds) {
        if (listIds == null || listIds.size() == 0) {
            return;
        }
        isIdphotoDao();
        idphotoDao.queryBuilder().where(IdphotoDao.Properties.Id.in(listIds))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 更新证件照
     */
    public Idphoto updateIdphoto(IdphotoBean idphotoBean, Idphoto idphoto) {
        if (idphotoBean == null || idphoto == null) {
            return null;
        }
        isIdphotoDao();
        idphoto.setName(idphotoBean.getName());
        idphoto.setMWidth(idphotoBean.getMWidth());
        idphoto.setMHeight(idphotoBean.getMHeight());
        idphoto.setPWidth(idphotoBean.getPWidth());
        idphoto.setPHeight(idphotoBean.getPHeight());
        idphoto.setDpi(idphotoBean.getDpi());
        idphoto.setPrice(idphotoBean.getPrice());
        idphoto.setEphoto(idphotoBean.getEphoto());
        idphoto.setTypesetting(idphotoBean.getTypesetting());
        idphoto.setBgColor(idphotoBean.getBgColor());
        idphoto.setFileSize(idphotoBean.getFileSize());
        idphoto.setFileFormat(idphotoBean.getFileFormat());
        idphoto.setIsPrint(idphotoBean.getIsPrint());
        idphoto.setPId(idphotoBean.getPId());
        idphoto.setRecommendTime(idphotoBean.getRecommendTime() > 0 ? idphotoBean.getRecommendTime() : 0);
        idphoto.setCloudUpdateTime(DateUtils.dateToStamp(idphotoBean.getUpdateTime()));
        idphotoDao.update(idphoto);
        return idphoto;
    }

    /**
     * 添加证件照
     */
    public Idphoto addIdphoto(IdphotoBean idphotoBean) {
        if (idphotoBean == null) {
            return null;
        }
        isIdphotoDao();
        Idphoto idphoto = new Idphoto();
        idphoto.setName(idphotoBean.getName());
        idphoto.setMWidth(idphotoBean.getMWidth());
        idphoto.setMHeight(idphotoBean.getMHeight());
        idphoto.setPWidth(idphotoBean.getPWidth());
        idphoto.setPHeight(idphotoBean.getPHeight());
        idphoto.setDpi(idphotoBean.getDpi());
        idphoto.setPrice(idphotoBean.getPrice());
        idphoto.setEphoto(idphotoBean.getEphoto());
        idphoto.setTypesetting(idphotoBean.getTypesetting());
        idphoto.setBgColor(idphotoBean.getBgColor());
        idphoto.setFileSize(idphotoBean.getFileSize());
        idphoto.setFileFormat(idphotoBean.getFileFormat());
        idphoto.setIsPrint(idphotoBean.getIsPrint());
        idphoto.setPId(idphotoBean.getPId());
        idphoto.setRecommendTime(idphotoBean.getRecommendTime() > 0 ? idphotoBean.getRecommendTime() : 0);
        idphoto.setCloudUpdateTime(DateUtils.dateToStamp(idphotoBean.getUpdateTime()));
        idphoto.setCreateTime(DateUtils.getCurrentTime());
        idphoto.setCloudId(idphotoBean.getId());
        idphotoDao.insert(idphoto);
        return idphoto;
    }

    /**
     * 获取证件照
     */
    public Idphoto getIdphotoByCloudID(long cloudId) {
        isIdphotoDao();
        return idphotoDao.queryBuilder().where(IdphotoDao.Properties.CloudId.eq(cloudId)).unique();
    }

    /**
     * 获取证件照
     */
    public Idphoto getIdphoto(long id) {
        isIdphotoDao();
        return idphotoDao.queryBuilder().where(IdphotoDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 获取证件照颜色列表
     */
    public List<IdphotoColor> getIdphotoColorLists(List<String> listIds) {
        isIdphotoColorDao();
        if (listIds == null || listIds.size() == 0) {
            return idphotoColorDao.queryBuilder()
                    .list();
        } else {
            return idphotoColorDao.queryBuilder().where(IdphotoColorDao.Properties.Id.in(listIds)).list();
        }
    }

    /**
     * 设置证件照颜色数据
     */
    public void setIdphotoColorData(List<IdphotoColorBean> listIdphotoColors) {
        if (listIdphotoColors == null || listIdphotoColors.size() == 0) {
            return;
        }
        List<IdphotoColor> idphotoColors = getIdphotoColorLists(null);
        if (idphotoColors == null) {
            idphotoColors = new ArrayList<>();
        }
        int len = idphotoColors.size();
        int size = listIdphotoColors.size();
        for (int i = 0; i < size; i++) {
            int j = 0;
            for (; j < len; j++) {
                if (idphotoColors.get(j).getCloudId() == listIdphotoColors.get(i).getId()) {
                    //检测更新
                    if (idphotoColors.get(j).getCloudUpdateTime() < DateUtils.dateToStamp(listIdphotoColors.get(i).getUpdateTime())) {
                        //更新
                        updateIdphotoColor(listIdphotoColors.get(i), idphotoColors.get(j));
                    }
                    idphotoColors.remove(j);
                    j--;
                    len--;
                    break;
                }
            }

            if (j == len) { //添加
                addIdphotoColor(listIdphotoColors.get(i));
            }
        }

        if (idphotoColors.size() > 0) {
            len = idphotoColors.size();
            List<Long> listIds = new ArrayList<>();
            for (int j = 0; j < len; j++) {
                listIds.add(idphotoColors.get(j).getId());
            }
            delIdphotoColor(listIds);
        }
    }

    /**
     * 删除证件照颜色
     */
    public void delIdphotoColor(List<Long> listIds) {
        if (listIds == null || listIds.size() == 0) {
            return;
        }
        isIdphotoColorDao();
        idphotoColorDao.queryBuilder().where(IdphotoColorDao.Properties.Id.in(listIds))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 添加证件照颜色
     */
    public IdphotoColor addIdphotoColor(IdphotoColorBean idphotoColorBean) {
        if (idphotoColorBean == null) {
            return null;
        }
        isIdphotoColorDao();
        IdphotoColor idphotoColor = new IdphotoColor();
        idphotoColor.setVal(idphotoColorBean.getVal());
        idphotoColor.setCloudId(idphotoColorBean.getId());
        idphotoColor.setCreateTime(DateUtils.getCurrentTime());
        idphotoColor.setCloudUpdateTime(DateUtils.dateToStamp(idphotoColorBean.getUpdateTime()));
        idphotoColorDao.insert(idphotoColor);
        return idphotoColor;
    }

    /**
     * 添加证件照颜色
     */
    public IdphotoColor updateIdphotoColor(IdphotoColorBean idphotoColorBean, IdphotoColor idphotoColor) {
        if (idphotoColorBean == null || idphotoColor == null) {
            return null;
        }
        isIdphotoColorDao();
        idphotoColor.setVal(idphotoColorBean.getVal());
        idphotoColor.setCloudUpdateTime(DateUtils.dateToStamp(idphotoColorBean.getUpdateTime()));
        idphotoColorDao.update(idphotoColor);
        return idphotoColor;
    }

    /**
     * 获取证件照颜色
     */
    public IdphotoColor getIdphotoColorByCloudID(long cloudId) {
        isIdphotoColorDao();
        return idphotoColorDao.queryBuilder().where(IdphotoColorDao.Properties.CloudId.eq(cloudId)).unique();
    }

    /**
     * 获取证件照颜色列表
     */
    public List<IdphotoColor> getIdphotoColorByCloudIDs(String colorIds) {
        if (StringUtils.isEmpty(colorIds)) {
            return null;
        }
        isIdphotoColorDao();
        String[] colorIdArr = colorIds.split(",");
        if (colorIdArr == null || colorIdArr.length == 0) {
            return null;
        }
        List<String> listIds = Arrays.asList(colorIdArr);
        try {
            return idphotoColorDao.queryBuilder().where(IdphotoColorDao.Properties.CloudId.in(listIds)).list();
        } catch (Exception e) {}
        return null;
    }

    /**
     * 获取证件照颜色
     */
    public IdphotoColor getIdphotoColor(long id) {
        isIdphotoColorDao();
        return idphotoColorDao.queryBuilder().where(IdphotoColorDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 设置证件照打印数据
     */
    public void setIdphotoPrintData(List<IdphotoPrintBean> listIdphotoPrints) {
        if (listIdphotoPrints == null || listIdphotoPrints.size() == 0) {
            return;
        }
        List<IdphotoPrint> idphotoPrints = getIdphotoPrintLists();
        if (idphotoPrints == null) {
            idphotoPrints = new ArrayList<>();
        }
        int len = idphotoPrints.size();
        int size = listIdphotoPrints.size();
        for (int i = 0; i < size; i++) {
            int j = 0;
            for (; j < len; j++) {
                if (idphotoPrints.get(j).getCloudId() == listIdphotoPrints.get(i).getId()) {
                    //检测更新
                    if (idphotoPrints.get(j).getCloudUpdateTime() < DateUtils.dateToStamp(listIdphotoPrints.get(i).getUpdateTime())) {
                        //更新
                        updateIdphotoPrint(listIdphotoPrints.get(i), idphotoPrints.get(j));
                    }
                    idphotoPrints.remove(j);
                    j--;
                    len--;
                    break;
                }
            }

            if (j == len) { //添加
                addIdphotoPrint(listIdphotoPrints.get(i));
            }
        }

        if (idphotoPrints.size() > 0) {
            len = idphotoPrints.size();
            List<Long> listIds = new ArrayList<>();
            for (int j = 0; j < len; j++) {
                listIds.add(idphotoPrints.get(j).getId());
            }
            delIdphotoPrint(listIds);
        }
    }

    /**
     * 删除打印数据
     */
    public void delIdphotoPrint(List<Long> listIds) {
        if (listIds == null || listIds.size() == 0) {
            return;
        }
        isIdphotoPrintDao();
        idphotoPrintDao.queryBuilder().where(IdphotoPrintDao.Properties.Id.in(listIds))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }


    /**
     * 获取证件照打印列表
     */
    public List<IdphotoPrint> getIdphotoPrintLists() {
        isIdphotoPrintDao();
        return idphotoPrintDao.queryBuilder()
                .list();
    }

    /**
     * 添加证件照打印
     */
    public IdphotoPrint addIdphotoPrint(IdphotoPrintBean idphotoPrintBean) {
        if (idphotoPrintBean == null) {
            return null;
        }
        isIdphotoPrintDao();
        IdphotoPrint idphotoPrint = new IdphotoPrint();
        idphotoPrint.setPNum(idphotoPrintBean.getPNum());
        idphotoPrint.setPPrice(idphotoPrintBean.getPPrice());
        idphotoPrint.setCloudId(idphotoPrintBean.getId());
        idphotoPrint.setCreateTime(DateUtils.getCurrentTime());
        idphotoPrint.setCloudUpdateTime(DateUtils.dateToStamp(idphotoPrintBean.getUpdateTime()));
        idphotoPrintDao.insert(idphotoPrint);
        return idphotoPrint;
    }

    /**
     * 添加证件照打印
     */
    public IdphotoPrint updateIdphotoPrint(IdphotoPrintBean idphotoPrintBean, IdphotoPrint idphotoPrint) {
        if (idphotoPrintBean == null || idphotoPrint == null) {
            return null;
        }
        isIdphotoPrintDao();
        idphotoPrint.setPNum(idphotoPrintBean.getPNum());
        idphotoPrint.setPPrice(idphotoPrintBean.getPPrice());
        idphotoPrint.setCloudUpdateTime(DateUtils.dateToStamp(idphotoPrintBean.getUpdateTime()));
        idphotoPrintDao.update(idphotoPrint);
        return idphotoPrint;
    }

    /**
     * 获取证件照打印
     */
    public IdphotoPrint getIdphotoPrintByCloudID(long cloudId) {
        isIdphotoPrintDao();
        return idphotoPrintDao.queryBuilder().where(IdphotoPrintDao.Properties.CloudId.eq(cloudId)).unique();
    }

    /**
     * 获取证件照打印
     */
    public IdphotoPrint getIdphotoPrint(long id) {
        isIdphotoPrintDao();
        return idphotoPrintDao.queryBuilder().where(IdphotoPrintDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 添加搜索关键词
     */
    public SearchRecord addSearckKeyWord(String keyWord, int type) {
        isSearchRecordDao();
        if (!StringUtils.isEmpty(keyWord)) {
            SearchRecord searchRecord = findSearchRecord(keyWord, type);
            if (searchRecord == null) {
                searchRecord = new SearchRecord();
                searchRecord.setCreateTime(DateUtils.getCurrentTime());
                searchRecord.setKeyWord(keyWord);
                searchRecord.setType(type);
                searchRecordDao.insert(searchRecord);
            } else {
                searchRecord.setCreateTime(DateUtils.getCurrentTime());
                searchRecordDao.update(searchRecord);
            }
            return searchRecord;
        }
        return null;
    }

    /**
     * 查找用户搜索关键词是否存在
     */
    private SearchRecord findSearchRecord(String keyWord, int type) {
        isSearchRecordDao();
        return searchRecordDao.queryBuilder()
                .where(SearchRecordDao.Properties.KeyWord.eq(keyWord), SearchRecordDao.Properties.Type.eq(type))
                .limit(1)
                .unique();
    }

    /**
     * 获取搜索记录
     */
    public List<SearchRecord> getSearchRecords(int type) {
        isSearchRecordDao();
        List<SearchRecord> searchRecords = searchRecordDao.queryBuilder()
                .where(SearchRecordDao.Properties.Type.eq(type))
                .orderDesc(SearchRecordDao.Properties.CreateTime)
                .limit(6)
                .list();
        if (searchRecords.size() >= 6) {
            searchRecordDao.queryBuilder()
                    .where(SearchRecordDao.Properties.CreateTime.lt(searchRecords.get(5).getCreateTime()), SearchRecordDao.Properties.Type.eq(type))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
        }
        return searchRecords;
    }

    /**
     * 删除某个用户所有搜索记录
     */
    public void delAllSearchRecord(int type) {
        isSearchRecordDao();
        searchRecordDao.queryBuilder().where(SearchRecordDao.Properties.Type.eq(type))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

}
