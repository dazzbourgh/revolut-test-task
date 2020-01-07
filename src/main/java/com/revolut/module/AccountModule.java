package com.revolut.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.revolut.controller.AccountController;
import com.revolut.dao.AccountDao;
import com.revolut.dao.impl.HashMapAccountDao;
import com.revolut.domain.Account;
import com.revolut.service.AccountCommandService;
import com.revolut.service.AccountQueryService;
import com.revolut.service.impl.AccountCommandServiceImpl;
import com.revolut.service.impl.AccountQueryServiceImpl;

import java.math.BigDecimal;

public class AccountModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountCommandService.class).to(AccountCommandServiceImpl.class);
        bind(AccountQueryService.class).to(AccountQueryServiceImpl.class);
        bind(AccountDao.class).to(HashMapAccountDao.class);
        bind(AccountController.class);
        var dataStoreBinder = MapBinder.newMapBinder(binder(), Long.class, Account.class);
        dataStoreBinder.addBinding(1L)
                .toInstance(new Account(1L, new BigDecimal(100)));
        dataStoreBinder.addBinding(2L)
                .toInstance(new Account(2L, new BigDecimal(50)));
    }
}
