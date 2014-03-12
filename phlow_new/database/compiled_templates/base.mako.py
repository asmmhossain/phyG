# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392809341.692051
_template_filename='templates/base.mako'
_template_uri='/base.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['stylesheets', 'init', 'javascripts', 'metas', 'title']


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
        __M_writer(u'\n<!DOCTYPE HTML>\n<html>\n    <!--base.mako-->\n    ')
        # SOURCE LINE 5
        __M_writer(unicode(self.init()))
        __M_writer(u'\n    <head>\n        <title>')
        # SOURCE LINE 7
        __M_writer(unicode(self.title()))
        __M_writer(u'</title>\n        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />\n        ')
        # SOURCE LINE 9
        __M_writer(unicode(self.metas()))
        __M_writer(u'\n        ')
        # SOURCE LINE 10
        __M_writer(unicode(self.stylesheets()))
        __M_writer(u'\n        ')
        # SOURCE LINE 11
        __M_writer(unicode(self.javascripts()))
        __M_writer(u'\n    </head>\n    <body class="inbound">\n        ')
        # SOURCE LINE 14
        __M_writer(unicode(next.body()))
        __M_writer(u'\n    </body>\n</html>\n\n')
        # SOURCE LINE 19
        __M_writer(u'\n\n')
        # SOURCE LINE 22
        __M_writer(u'\n\n')
        # SOURCE LINE 27
        __M_writer(u'\n\n')
        # SOURCE LINE 94
        __M_writer(u'\n\n')
        # SOURCE LINE 97
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_stylesheets(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 25
        __M_writer(u'\n    ')
        # SOURCE LINE 26
        __M_writer(unicode(h.css('base')))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_init(context):
    context.caller_stack._push_frame()
    try:
        __M_writer = context.writer()
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
        # SOURCE LINE 30
        __M_writer(u'\n    \n')
        # SOURCE LINE 33
        if app.config.sentry_dsn:
            # SOURCE LINE 34
            __M_writer(u'        ')
            __M_writer(unicode(h.js( "libs/tracekit", "libs/raven" )))
            __M_writer(u"\n        <script>\n            Raven.config('")
            # SOURCE LINE 36
            __M_writer(unicode(app.config.sentry_dsn_public))
            __M_writer(u"').install();\n")
            # SOURCE LINE 37
            if trans.user:
                # SOURCE LINE 38
                __M_writer(u'                Raven.setUser( { email: "')
                __M_writer(unicode(trans.user.email))
                __M_writer(u'" } );\n')
                pass
            # SOURCE LINE 40
            __M_writer(u'        </script>\n')
            pass
        # SOURCE LINE 42
        __M_writer(u'\n    ')
        # SOURCE LINE 43
        __M_writer(unicode(h.js(
        "libs/jquery/jquery",
        "libs/jquery/jquery.migrate",
        "libs/jquery/select2",
        "libs/bootstrap",
        "libs/underscore",
        "libs/backbone/backbone",
        "libs/handlebars.runtime",
        "galaxy.base",
        "mvc/ui",
        'libs/require'
    )))
        # SOURCE LINE 54
        __M_writer(u'\n\n    <script type="text/javascript">\n')
        # SOURCE LINE 58
        __M_writer(u"        var galaxy_config =\n        {\n            root: '")
        # SOURCE LINE 60
        __M_writer(unicode(h.url_for( "/" )))
        __M_writer(u"'\n        };\n\n        // console protection\n        window.console = window.console || {\n            log     : function(){},\n            debug   : function(){},\n            info    : function(){},\n            warn    : function(){},\n            error   : function(){},\n            assert  : function(){}\n        };\n\n")
        # SOURCE LINE 74
        __M_writer(u'        require.config({\n            baseUrl: "')
        # SOURCE LINE 75
        __M_writer(unicode(h.url_for('/static/scripts') ))
        __M_writer(u'",\n            shim: {\n                "libs/underscore": { exports: "_" },\n                "libs/backbone/backbone": { exports: "Backbone" }\n            }\n        });\n    </script>\n\n')
        # SOURCE LINE 83
        if not form_input_auto_focus is UNDEFINED and form_input_auto_focus:
            # SOURCE LINE 84
            __M_writer(u'        <script type="text/javascript">\n            $(document).ready( function() {\n                // Auto Focus on first item on form\n                if ( $("*:focus").html() == null ) {\n                    $(":input:not([type=hidden]):visible:enabled:first").focus();\n                }\n            });\n        </script>\n')
            pass
        # SOURCE LINE 93
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


