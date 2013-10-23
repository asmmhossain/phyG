# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1382433163.290202
_template_filename=u'templates/webapps/galaxy/visualization/trackster_common.mako'
_template_uri=u'/visualization/trackster_common.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['render_trackster_css_files', 'render_trackster_js_files', 'render_trackster_js_vars']


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        __M_writer = context.writer()
        # SOURCE LINE 4
        __M_writer(u'\n')
        # SOURCE LINE 9
        __M_writer(u'\n\n\n')
        # SOURCE LINE 16
        __M_writer(u'\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_trackster_css_files(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 6
        __M_writer(u'\n    ')
        # SOURCE LINE 7
        __M_writer(unicode(h.css( "history", "autocomplete_tagging", "trackster", "library", 
             "jquery-ui/smoothness/jquery-ui" )))
        # SOURCE LINE 8
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_trackster_js_files(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 13
        __M_writer(u'\n    ')
        # SOURCE LINE 14
        __M_writer(unicode(h.js( "galaxy.panels", "libs/jquery/jstorage", "libs/jquery/jquery.event.drag", "libs/jquery/jquery.event.hover","libs/jquery/jquery.mousewheel", "libs/jquery/jquery-ui", "libs/jquery/jquery-ui-combobox",
    	"libs/require", "libs/farbtastic" )))
        # SOURCE LINE 15
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_trackster_js_vars(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 19
        __M_writer(u'\n        add_track_async_url = "')
        # SOURCE LINE 20
        __M_writer(unicode(h.url_for( controller='/api/datasets' )))
        __M_writer(u'";\n        select_datasets_url = "')
        # SOURCE LINE 21
        __M_writer(unicode(h.url_for( controller='/visualization', action='list_current_history_datasets' )))
        __M_writer(u'";\n        reference_url = "')
        # SOURCE LINE 22
        __M_writer(unicode(h.url_for( controller='/api/genomes' )))
        __M_writer(u'";\n        chrom_url = "')
        # SOURCE LINE 23
        __M_writer(unicode(h.url_for( controller='/api/genomes' )))
        __M_writer(u'";\n        datasets_url = "')
        # SOURCE LINE 24
        __M_writer(unicode(h.url_for( controller='/api/datasets' )))
        __M_writer(u'";\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


