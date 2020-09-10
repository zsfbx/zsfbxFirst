package cn.smbms.dao.provider;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {

    /**
     * 增加供应商
     * @param provider
     * @return
     * @
     */
    int add(Provider provider);

    /**
     * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
     * @param proName
     * @return
     * @
     */
    List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode);

    /**
     * 通过proId删除Provider
     * @param delId
     * @return
     * @
     */
    int deleteProviderById(Integer delId);

    /**
     * 通过proId获取Provider
     * @param id
     * @return
     * @
     */
    Provider getProviderById(Integer id);

    /**
     * 修改用户信息
     * @param provider
     * @return
     * @
     */
    int modify(Provider provider);

}
