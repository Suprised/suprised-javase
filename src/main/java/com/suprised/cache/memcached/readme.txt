常用方法
一般缓存数据的常用操作有：set(add+replace)、get、replace、add

public Future<Boolean> set(String key, int exp, Object o)  

第一个参数：键（key）
第二个参数：过期时间（单位是秒）
第三个参数：要设置缓存中的对象（value），如果没有则插入，如果有则修改。

public Object get(String key)  

第一个参数：键（key）

public Future<Boolean> replace(String key, int exp, Object o)  

第一个参数：键（key）
第二个参数：过期时间（单位是秒）
第三个参数：该键的新值（new value），如果有则修改。

public Future<Boolean> add(String key, int exp, Object o)  

第一个参数：键（key）
第二个参数：过期时间（单位是秒）
第三个参数：该键的值（value），如果没有则插入。