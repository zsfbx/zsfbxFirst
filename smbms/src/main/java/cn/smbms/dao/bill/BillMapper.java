package cn.smbms.dao.bill;

import cn.smbms.pojo.Bill;

import java.util.List;

public interface BillMapper {
    /**
     * 增加订单
     * @param bill
     * @return
     * @
     */
    int add(Bill bill);

    /**
     * 通过查询条件获取供应商列表-模糊查询-getBillList
     * @param bill
     * @return
     * @
     */
    List<Bill> getBillList(Bill bill);

    /**
     * 通过delId删除Bill
     * @param delId
     * @return
     * @
     */
    int deleteBillById(Integer delId);

    /**
     * 通过billId获取Bill
     * @param id
     * @return
     * @
     */
    Bill getBillById(Integer id);

    /**
     * 修改订单信息
     * @param bill
     * @return
     * @
     */
    int modify(Bill bill);

    /**
     * 根据供应商ID查询订单数量
     * @param providerId
     * @return
     * @
     */
    int getBillCountByProviderId(Integer providerId);

}
