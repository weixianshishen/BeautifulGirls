package wxb.beautifulgirls.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import wxb.beautifulgirls.data.entity.Girl;

import wxb.beautifulgirls.db.dao.GirlDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig girlDaoConfig;

    private final GirlDao girlDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        girlDaoConfig = daoConfigMap.get(GirlDao.class).clone();
        girlDaoConfig.initIdentityScope(type);

        girlDao = new GirlDao(girlDaoConfig, this);

        registerDao(Girl.class, girlDao);
    }
    
    public void clear() {
        girlDaoConfig.clearIdentityScope();
    }

    public GirlDao getGirlDao() {
        return girlDao;
    }

}
