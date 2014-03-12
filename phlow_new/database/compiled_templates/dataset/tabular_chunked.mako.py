# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392818889.093916
_template_filename='templates/webapps/galaxy/dataset/tabular_chunked.mako'
_template_uri='/dataset/tabular_chunked.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['stylesheets', 'javascripts', 'title']


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 2
    ns = runtime.TemplateNamespace('__anon_0x8285490', context._clean_inheritance_tokens(), templateuri=u'/dataset/display.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0x8285490')] = ns

def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, u'/base.mako', _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x8285490')._populate(_import_ns, [u'render_deleted_data_message'])
        render_deleted_data_message = _import_ns.get('render_deleted_data_message', context.get('render_deleted_data_message', UNDEFINED))
        dataset = _import_ns.get('dataset', context.get('dataset', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 1
        __M_writer(u'\n')
        # SOURCE LINE 2
        __M_writer(u'\n\n')
        # SOURCE LINE 4
        __M_writer(u'\n\n')
        # SOURCE LINE 34
        __M_writer(u'\n\n')
        # SOURCE LINE 38
        __M_writer(u'\n\n')
        # SOURCE LINE 40
        __M_writer(unicode( render_deleted_data_message( dataset ) ))
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_stylesheets(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x8285490')._populate(_import_ns, [u'render_deleted_data_message'])
        parent = _import_ns.get('parent', context.get('parent', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 36
        __M_writer(u'\n    ')
        # SOURCE LINE 37
        __M_writer(unicode(parent.stylesheets()))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_javascripts(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x8285490')._populate(_import_ns, [u'render_deleted_data_message'])
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        chunk = _import_ns.get('chunk', context.get('chunk', UNDEFINED))
        trans = _import_ns.get('trans', context.get('trans', UNDEFINED))
        parent = _import_ns.get('parent', context.get('parent', UNDEFINED))
        dataset = _import_ns.get('dataset', context.get('dataset', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 6
        __M_writer(u'\n    ')
        # SOURCE LINE 7
        __M_writer(unicode(parent.javascripts()))
        __M_writer(u'\n    ')
        # SOURCE LINE 8
        __M_writer(unicode(h.js( "libs/require" )))
        __M_writer(u'\n    ')
        # SOURCE LINE 9
        __M_writer(unicode(h.templates("helpers-common-templates")))
        __M_writer(u'\n\n    <script type="text/javascript">\n        require.config({ \n            baseUrl: "')
        # SOURCE LINE 13
        __M_writer(unicode(h.url_for('/static/scripts')))
        __M_writer(u'",\n            shim: {\n                "libs/backbone/backbone": { exports: "Backbone" },\n            }\n        });\n\n        require([\'mvc/data\'], function(data) {\n            data.createTabularDatasetChunkedView(\n                _.extend( ')
        # SOURCE LINE 21
        __M_writer(unicode(h.to_json_string( trans.security.encode_dict_ids( dataset.to_dict() ) )))
        __M_writer(u', \n                        {\n                            url_viz: "')
        # SOURCE LINE 23
        __M_writer(unicode(h.url_for( controller='/visualization')))
        __M_writer(u'",\n                            chunk_url: "')
        # SOURCE LINE 24
        __M_writer(unicode(h.url_for( controller='/dataset', action='display',
                                             dataset_id=trans.security.encode_id( dataset.id ))))
        # SOURCE LINE 25
        __M_writer(u'",\n                            first_data_chunk: ')
        # SOURCE LINE 26
        __M_writer(unicode(chunk))
        __M_writer(u"\n                        } \n                ),\n                // Append view to body.\n                $('body')\n            );\n        });\n    </script>\n")
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_title(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x8285490')._populate(_import_ns, [u'render_deleted_data_message'])
        __M_writer = context.writer()
        # SOURCE LINE 4
        __M_writer(u'Dataset Display')
        return ''
    finally:
        context.caller_stack._pop_frame()


