# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1381349574.968407
_template_filename=u'templates/refresh_frames.mako'
_template_uri=u'/refresh_frames.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['handle_refresh_frames']


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        __M_writer = context.writer()
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_handle_refresh_frames(context):
    context.caller_stack._push_frame()
    try:
        int = context.get('int', UNDEFINED)
        h = context.get('h', UNDEFINED)
        app = context.get('app', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        refresh_frames = context.get('refresh_frames', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 2
        __M_writer(u'\n')
        # SOURCE LINE 4
        __M_writer(u'    ')

        if not refresh_frames: return ''
            
        
        # SOURCE LINE 6
        __M_writer(u'\n\n')
        # SOURCE LINE 9
        __M_writer(u'    <script type="text/javascript">\n')
        # SOURCE LINE 10
        if 'everything' in refresh_frames:
            # SOURCE LINE 11
            __M_writer(u'        parent.location.href="')
            __M_writer(unicode(h.url_for( controller='root' )))
            __M_writer(u'";\n')
            pass
        # SOURCE LINE 13
        if 'masthead' in refresh_frames:
            # SOURCE LINE 20
            __M_writer(u'        \n')
            # SOURCE LINE 22
            __M_writer(u'        if ( parent.user_changed ) {\n')
            # SOURCE LINE 23
            if trans.user:
                # SOURCE LINE 24
                __M_writer(u'                parent.user_changed( "')
                __M_writer(unicode(trans.user.email))
                __M_writer(u'", ')
                __M_writer(unicode(int( app.config.is_admin_user( trans.user ) )))
                __M_writer(u' );\n')
                # SOURCE LINE 25
            else:
                # SOURCE LINE 26
                __M_writer(u'                parent.user_changed( null, false );\n')
                pass
            # SOURCE LINE 28
            __M_writer(u'        }\n')
            pass
        # SOURCE LINE 30
        if 'history' in refresh_frames:
            # SOURCE LINE 31
            __M_writer(u'        if ( parent.frames && parent.frames.galaxy_history ) {\n            parent.frames.galaxy_history.location.href="')
            # SOURCE LINE 32
            __M_writer(unicode(h.url_for( controller='root', action='history')))
            __M_writer(u'";\n            if ( parent.force_right_panel ) {\n                parent.force_right_panel( \'show\' );\n            }\n        }\n')
            pass
        # SOURCE LINE 38
        if 'tools' in refresh_frames:
            # SOURCE LINE 39
            __M_writer(u"        if ( parent.frames && Galaxy.toolPanel ) {\n            // FIXME: refreshing the tool menu does not work with new JS-based approach, \n            // but refreshing the tool menu is not used right now, either.\n\n            if ( parent.force_left_panel ) {\n                parent.force_left_panel( 'show' );\n            }\n        }\n")
            pass
        # SOURCE LINE 48
        __M_writer(u'    </script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


