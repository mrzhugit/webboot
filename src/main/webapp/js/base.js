/**
 * 加载页面
 * @param pageUrl 需要加载的页面url
 * @param appendId 加载的页面要拼接的元素ID
 */
function loadPage(pageUrl, appendId) {
    $.ajax({
        type: "POST",
        url: pageUrl,
        success: function (html) {
            $(appendId).html(html);
        },
        error: function (msg) {
            alert("网络出现异常,请重试");
        }
    });
}