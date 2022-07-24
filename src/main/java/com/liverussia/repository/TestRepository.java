package com.liverussia.repository;

import com.liverussia.dao.TestEntity;


public interface TestRepository  {
    TestEntity getById(String id);
}
