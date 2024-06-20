package com.ivmiku.w6r1.filter;

import cn.dev33.satoken.same.SaSameUtil;
import org.apache.dubbo.rpc.*;

public class SaFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getClientAttachment().setAttachment(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
        return invoker.invoke(invocation);
    }
}
