# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392805205.766279
_template_filename=u'templates/webapps/galaxy/utils/localization.mako'
_template_uri=u'/utils/localization.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['localize_js_strings']


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        __M_writer = context.writer()
        # SOURCE LINE 12
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_localize_js_strings(context,strings_to_localize):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        dict = context.get('dict', UNDEFINED)
        _ = context.get('_', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 1
        __M_writer(u'\n')
        # SOURCE LINE 5
        __M_writer(u'<script type="text/javascript">\n')
        # SOURCE LINE 8
        __M_writer(u'    GalaxyLocalization.setLocalizedString(\n        ')
        # SOURCE LINE 9
        __M_writer(unicode( h.to_json_string( dict([ ( string, _(string) ) for string in strings_to_localize ]) ) ))
        __M_writer(u'\n    );\n</script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


