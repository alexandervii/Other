package com.alex.db;

import com.alex.model.ThreadInfo;

import java.util.List;

/**
 * 数据访问接口
 */
public interface ThreadDao {

    /**
     * 插入一条线程信息
     * @param info
     */
    public void insertThread(ThreadInfo info);

    /**
     * 删除线程
     * @param url
     * @param threadId
     */
    public void deleteThread(String url, int threadId);

    /**
     * 更新线程
     * @param url
     * @param threadId
     * @param finished
     */
    public void updateThread(String url, int threadId, int finished);

    /**
     * 获取所有的线程
     * @return
     */
    public List<ThreadInfo> getAllThreads(String url);

    /**
     * 判断下载线程是否存在
     * @param url
     * @param threadId
     * @return
     */
    public boolean isExists(String url, int threadId);
}
