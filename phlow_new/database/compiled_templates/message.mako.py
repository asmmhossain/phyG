# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392809341.68331
_template_filename=u'templates/message.mako'
_template_uri=u'/message.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['body', 'render_msg', 'center_panel', 'init', 'render_large_message', 'javascripts']


# SOURCE LINE 1

def inherit(context):
    if context.get('use_panels'):
        if context.get('webapp'):
            webapp = context.get('webapp')
        else:
            webapp = 'galaxy'
        return '/webapps/%s/base_panels.mako' % webapp
    else:
        return '/base.mako'


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 14
    ns = runtime.TemplateNamespace('__anon_0x7c3e5d0', context._clean_inheritance_tokens(), templateuri=u'/refresh_frames.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0x7c3e5d0')] = ns

def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, (inherit(context)), _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        n_ = _import_ns.get('n_', context.get('n_', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 11
        __M_writer(u'\n')
        # SOURCE LINE 12
        __M_writer(u'\n\n')
        # SOURCE LINE 14
        __M_writer(u'\n\n')
        # SOURCE LINE 16
        _=n_ 
        
        __M_locals_builtin_stored = __M_locals_builtin()
        __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['_'] if __M_key in __M_locals_builtin_stored]))
        __M_writer(u'\n\n')
        # SOURCE LINE 25
        __M_writer(u'\n\n')
        # SOURCE LINE 36
        __M_writer(u'\n\n')
        # SOURCE LINE 41
        __M_writer(u'\n')
        # SOURCE LINE 44
        __M_writer(u'\n\n')
        # SOURCE LINE 48
        __M_writer(u'\n\n')
        # SOURCE LINE 53
        __M_writer(u'\n\n')
        # SOURCE LINE 59
        __M_writer(u'\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_body(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        status = _import_ns.get('status', context.get('status', UNDEFINED))
        message = _import_ns.get('message', context.get('message', UNDEFINED))
        def render_large_message(message,status):
            return render_render_large_message(context,message,status)
        __M_writer = context.writer()
        # SOURCE LINE 46
        __M_writer(u'\n    ')
        # SOURCE LINE 47
        __M_writer(unicode(render_large_message( message, status )))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_msg(context,msg,status='done'):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        _ = _import_ns.get('_', context.get('_', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 56
        __M_writer(u'\n    <div class="')
        # SOURCE LINE 57
        __M_writer(unicode(status))
        __M_writer(u'message">')
        __M_writer(unicode(_(msg)))
        __M_writer(u'</div>\n    <br/>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_center_panel(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        status = _import_ns.get('status', context.get('status', UNDEFINED))
        message = _import_ns.get('message', context.get('message', UNDEFINED))
        def render_large_message(message,status):
            return render_render_large_message(context,message,status)
        __M_writer = context.writer()
        # SOURCE LINE 42
        __M_writer(u'\n    ')
        # SOURCE LINE 43
        __M_writer(unicode(render_large_message( message, status )))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_init(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        self = _import_ns.get('self', context.get('self', UNDEFINED))
        active_view = _import_ns.get('active_view', context.get('active_view', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 18
        __M_writer(u'\n')
        # SOURCE LINE 19

        self.has_left_panel=False
        self.has_right_panel=False
        self.active_view=active_view
        self.message_box_visible=False
        
        
        # SOURCE LINE 24
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_large_message(context,message,status):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        _ = _import_ns.get('_', context.get('_', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 51
        __M_writer(u'\n    <div class="')
        # SOURCE LINE 52
        __M_writer(unicode(status))
        __M_writer(u'messagelarge" style="margin: 1em">')
        __M_writer(unicode(_(message)))
        __M_writer(u'</div>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_javascripts(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x7c3e5d0')._populate(_import_ns, [u'handle_refresh_frames'])
        handle_refresh_frames = _import_ns.get('handle_refresh_frames', context.get('handle_refresh_frames', UNDEFINED))
        parent = _import_ns.get('parent', context.get('parent', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 27
        __M_writer(u'\n    ')
        # SOURCE LINE 28
        __M_writer(unicode(parent.javascripts()))
        __M_writer(u'\n    ')
        # SOURCE LINE 29
        __M_writer(unicode(handle_refresh_frames()))
        __M_writer(u'\n    <script type="text/javascript">\n        if ( parent.handle_minwidth_hint )\n        {\n            parent.handle_minwidth_hint( -1 );\n        }\n    </script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


