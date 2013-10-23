# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1381256488.459495
_template_filename=u'templates/base.mako'
_template_uri=u'/base.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['stylesheets', 'javascripts', 'metas', 'title']


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        self = context.get('self', UNDEFINED)
        next = context.get('next', UNDEFINED)
        n_ = context.get('n_', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 1
        _=n_ 
        
        __M_locals_builtin_stored = __M_locals_builtin()
        __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['_'] if __M_key in __M_locals_builtin_stored]))
        __M_writer(u'\n<!DOCTYPE HTML>\n<html>\n    <!--base.mako-->\n    <head>\n        <title>')
        # SOURCE LINE 6
        __M_writer(unicode(self.title()))
        __M_writer(u'</title>\n        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />\n        ')
        # SOURCE LINE 8
        __M_writer(unicode(self.metas()))
        __M_writer(u'\n        ')
        # SOURCE LINE 9
        __M_writer(unicode(self.stylesheets()))
        __M_writer(u'\n        ')
        # SOURCE LINE 10
        __M_writer(unicode(self.javascripts()))
        __M_writer(u'\n    </head>\n    <body>\n        ')
        # SOURCE LINE 13
        __M_writer(unicode(next.body()))
        __M_writer(u'\n    </body>\n</html>\n\n')
        # SOURCE LINE 18
        __M_writer(u'\n\n')
        # SOURCE LINE 23
        __M_writer(u'\n\n')
        # SOURCE LINE 89
        __M_writer(u'\n\n')
        # SOURCE LINE 92
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_stylesheets(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 21
        __M_writer(u'\n    ')
        # SOURCE LINE 22
        __M_writer(unicode(h.css('base')))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_javascripts(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        app = context.get('app', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        form_input_auto_focus = context.get('form_input_auto_focus', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 26
        __M_writer(u'\n    \n')
        # SOURCE LINE 29
        if app.config.sentry_dsn:
            # SOURCE LINE 30
            __M_writer(u'        ')
            __M_writer(unicode(h.js( "libs/tracekit", "libs/raven" )))
            __M_writer(u"\n        <script>\n            Raven.config('")
            # SOURCE LINE 32
            __M_writer(unicode(app.config.sentry_dsn_public))
            __M_writer(u"').install();\n")
            # SOURCE LINE 33
            if trans.user:
                # SOURCE LINE 34
                __M_writer(u'                Raven.setUser( { email: "')
                __M_writer(unicode(trans.user.email))
                __M_writer(u'" } );\n')
                pass
            # SOURCE LINE 36
            __M_writer(u'        </script>\n')
            pass
        # SOURCE LINE 38
        __M_writer(u'\n    ')
        # SOURCE LINE 39
        __M_writer(unicode(h.js(
        "libs/jquery/jquery",
        "libs/jquery/jquery.migrate",
        "libs/jquery/select2",
        "libs/json2",
        "libs/bootstrap",
        "libs/underscore",
        "libs/backbone/backbone",
        "libs/backbone/backbone-relational",
        "libs/handlebars.runtime",
        "galaxy.base"
    )))
        # SOURCE LINE 50
        __M_writer(u'\n\n    ')
        # SOURCE LINE 52
        __M_writer(unicode(h.templates(
        "template-popupmenu-menu"
    )))
        # SOURCE LINE 54
        __M_writer(u'\n\n    ')
        # SOURCE LINE 56
        __M_writer(unicode(h.js(
        "mvc/ui"
    )))
        # SOURCE LINE 58
        __M_writer(u'\n\n    <script type="text/javascript">\n        // console protection\n        window.console = window.console || {\n            log     : function(){},\n            debug   : function(){},\n            info    : function(){},\n            warn    : function(){},\n            error   : function(){},\n            assert  : function(){}\n        };\n\n        // Set up needed paths.\n        var galaxy_paths = new GalaxyPaths({\n            root_path: \'')
        # SOURCE LINE 73
        __M_writer(unicode(h.url_for( "/" )))
        __M_writer(u"',\n            image_path: '")
        # SOURCE LINE 74
        __M_writer(unicode(h.url_for( "/static/images" )))
        __M_writer(u"'\n        });\n    </script>\n\n")
        # SOURCE LINE 78
        if not form_input_auto_focus is UNDEFINED and form_input_auto_focus:
            # SOURCE LINE 79
            __M_writer(u'        <script type="text/javascript">\n            $(document).ready( function() {\n                // Auto Focus on first item on form\n                if ( $("*:focus").html() == null ) {\n                    $(":input:not([type=hidden]):visible:enabled:first").focus();\n                }\n            });\n        </script>\n')
            pass
        # SOURCE LINE 88
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_metas(context):
    context.caller_stack._push_frame()
    try:
        __M_writer = context.writer()
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_title(context):
    context.caller_stack._push_frame()
    try:
        __M_writer = context.writer()
        return ''
    finally:
        context.caller_stack._pop_frame()


