# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1394553530.695702
_template_filename='templates/user/login.mako'
_template_uri='/user/login.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['body', 'init', 'render_openid_form', 'center_panel', 'render_login_form']


# SOURCE LINE 1

#This is a hack, we should restructure templates to avoid this.
def inherit(context):
    if context.get('trans').webapp.name == 'galaxy' and context.get( 'use_panels', True ):
        return '/webapps/galaxy/base_panels.mako'
    elif context.get('trans').webapp.name == 'tool_shed' and context.get( 'use_panels', True ):
        return '/webapps/tool_shed/base_panels.mako'
    else:
        return '/base.mako'


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 23
    ns = runtime.TemplateNamespace('__anon_0x85dc3d0', context._clean_inheritance_tokens(), templateuri=u'/message.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0x85dc3d0')] = ns

def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, (inherit(context)), _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x85dc3d0')._populate(_import_ns, [u'render_msg'])
        __M_writer = context.writer()
        # SOURCE LINE 10
        __M_writer(u'\n\n')
        # SOURCE LINE 12
        __M_writer(u'\n\n')
        # SOURCE LINE 21
        __M_writer(u'\n\n')
        # SOURCE LINE 23
        __M_writer(u'\n\n')
        # SOURCE LINE 27
        __M_writer(u'\n\n')
        # SOURCE LINE 67
        __M_writer(u'\n\n')
        # SOURCE LINE 100
        __M_writer(u'\n\n')
        # SOURCE LINE 124
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_body(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x85dc3d0')._populate(_import_ns, [u'render_msg'])
        status = _import_ns.get('status', context.get('status', UNDEFINED))
        redirect = _import_ns.get('redirect', context.get('redirect', UNDEFINED))
        render_msg = _import_ns.get('render_msg', context.get('render_msg', UNDEFINED))
        redirect_url = _import_ns.get('redirect_url', context.get('redirect_url', UNDEFINED))
        def render_openid_form(redirect,auto_associate,openid_providers):
            return render_render_openid_form(context,redirect,auto_associate,openid_providers)
        def render_login_form(form_action=None):
            return render_render_login_form(context,form_action)
        openid_providers = _import_ns.get('openid_providers', context.get('openid_providers', UNDEFINED))
        message = _import_ns.get('message', context.get('message', UNDEFINED))
        trans = _import_ns.get('trans', context.get('trans', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 29
        __M_writer(u'\n\n')
        # SOURCE LINE 31
        if redirect_url:
            # SOURCE LINE 32
            __M_writer(u'        <script type="text/javascript">  \n            top.location.href = \'')
            # SOURCE LINE 33
            __M_writer(filters.html_escape(unicode(redirect_url )))
            __M_writer(u"';\n        </script>\n")
            pass
        # SOURCE LINE 36
        __M_writer(u'\n')
        # SOURCE LINE 37
        if context.get('use_panels'):
            # SOURCE LINE 38
            __M_writer(u'        <div style="margin: 1em;">\n')
            # SOURCE LINE 39
        else:
            # SOURCE LINE 40
            __M_writer(u'        <div>\n')
            pass
        # SOURCE LINE 42
        __M_writer(u'\n')
        # SOURCE LINE 43
        if message:
            # SOURCE LINE 44
            __M_writer(u'        ')
            __M_writer(unicode(render_msg( message, status )))
            __M_writer(u'\n')
            pass
        # SOURCE LINE 46
        __M_writer(u'\n')
        # SOURCE LINE 47
        if not trans.user:
            # SOURCE LINE 48
            __M_writer(u'\n        ')
            # SOURCE LINE 49
            __M_writer(unicode(render_login_form()))
            __M_writer(u'\n\n')
            # SOURCE LINE 51
            if trans.app.config.enable_openid:
                # SOURCE LINE 52
                __M_writer(u'            <br/>\n            ')
                # SOURCE LINE 53
                __M_writer(unicode(render_openid_form( redirect, False, openid_providers )))
                __M_writer(u'\n')
                pass
            # SOURCE LINE 55
            __M_writer(u'\n')
            # SOURCE LINE 56
            if trans.app.config.get( 'terms_url', None ) is not None:
                # SOURCE LINE 57
                __M_writer(u'            <br/>\n            <p>\n                <a href="')
                # SOURCE LINE 59
                __M_writer(unicode(trans.app.config.get('terms_url', None)))
                __M_writer(u'">Terms and Conditions for use of this service</a>\n            </p>\n')
                pass
            # SOURCE LINE 62
            __M_writer(u'\n')
            pass
        # SOURCE LINE 64
        __M_writer(u'\n    </div>\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_init(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x85dc3d0')._populate(_import_ns, [u'render_msg'])
        self = _import_ns.get('self', context.get('self', UNDEFINED))
        active_view = _import_ns.get('active_view', context.get('active_view', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 14
        __M_writer(u'\n')
        # SOURCE LINE 15

        self.has_left_panel=False
        self.has_right_panel=False
        self.active_view=active_view
        self.message_box_visible=False
        
        
        # SOURCE LINE 20
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_openid_form(context,redirect,auto_associate,openid_providers):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x85dc3d0')._populate(_import_ns, [u'render_msg'])
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 102
        __M_writer(u'\n    <div class="toolForm">\n        <div class="toolFormTitle">OpenID Login</div>\n        <form name="openid" id="openid" action="')
        # SOURCE LINE 105
        __M_writer(unicode(h.url_for( controller='user', action='openid_auth' )))
        __M_writer(u'" method="post" target="_parent" >\n            <div class="form-row">\n                <label>OpenID URL:</label>\n                <input type="text" name="openid_url" size="60" style="background-image:url(\'')
        # SOURCE LINE 108
        __M_writer(unicode(h.url_for( '/static/images/openid-16x16.gif' )))
        __M_writer(u'\' ); background-repeat: no-repeat; padding-right: 20px; background-position: 99% 50%;"/>\n                <input type="hidden" name="redirect" value="')
        # SOURCE LINE 109
        __M_writer(filters.html_escape(unicode(redirect )))
        __M_writer(u'" size="40"/>\n            </div>\n            <div class="form-row">\n                Or, authenticate with your <select name="openid_provider">\n')
        # SOURCE LINE 113
        for provider in openid_providers:
            # SOURCE LINE 114
            __M_writer(u'                    <option value="')
            __M_writer(unicode(provider.id))
            __M_writer(u'">')
            __M_writer(unicode(provider.name))
            __M_writer(u'</option>\n')
            pass
        # SOURCE LINE 116
        __M_writer(u'                </select> account.\n            </div>\n            <div class="form-row">\n                <input type="submit" name="login_button" value="Login"/>\n            </div>\n        </form>\n    </div>\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_center_panel(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x85dc3d0')._populate(_import_ns, [u'render_msg'])
        def body():
            return render_body(context)
        __M_writer = context.writer()
        # SOURCE LINE 25
        __M_writer(u'\n    ')
        # SOURCE LINE 26
        __M_writer(unicode(body()))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_login_form(context,form_action=None):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x85dc3d0')._populate(_import_ns, [u'render_msg'])
        redirect = _import_ns.get('redirect', context.get('redirect', UNDEFINED))
        header = _import_ns.get('header', context.get('header', UNDEFINED))
        use_panels = _import_ns.get('use_panels', context.get('use_panels', UNDEFINED))
        email = _import_ns.get('email', context.get('email', UNDEFINED))
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 69
        __M_writer(u'\n\n    ')
        # SOURCE LINE 71

        if form_action is None:
            form_action = h.url_for( controller='user', action='login', use_panels=use_panels )
            
        
        # SOURCE LINE 74
        __M_writer(u'\n\n')
        # SOURCE LINE 76
        if header:
            # SOURCE LINE 77
            __M_writer(u'        ')
            __M_writer(unicode(header))
            __M_writer(u'\n')
            pass
        # SOURCE LINE 79
        __M_writer(u'    <div class="toolForm">\n        <div class="toolFormTitle">Login</div>\n        <form name="login" id="login" action="')
        # SOURCE LINE 81
        __M_writer(unicode(form_action))
        __M_writer(u'" method="post" >\n            <div class="form-row">\n                <label>Email address:</label>\n                <input type="text" name="email" value="')
        # SOURCE LINE 84
        __M_writer(filters.html_escape(unicode(email )))
        __M_writer(u'" size="40"/>\n                <input type="hidden" name="redirect" value="')
        # SOURCE LINE 85
        __M_writer(filters.html_escape(unicode(redirect )))
        __M_writer(u'" size="40"/>\n            </div>\n            <div class="form-row">\n                <label>Password:</label>\n                <input type="password" name="password" value="" size="40"/>\n                <div class="toolParamHelp" style="clear: both;">\n                    <a href="')
        # SOURCE LINE 91
        __M_writer(unicode(h.url_for( controller='user', action='reset_password', use_panels=use_panels )))
        __M_writer(u'">Forgot password? Reset here</a>\n                </div>\n            </div>\n            <div class="form-row">\n                <input type="submit" name="login_button" value="Login"/>\n            </div>\n        </form>\n    </div>\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


