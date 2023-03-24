package com.alpgo.common.exception.user;

import com.alpgo.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author alpgo
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
