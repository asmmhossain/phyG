# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1382433163.045489
_template_filename='templates/webapps/galaxy/dataset/large_file.mako'
_template_uri='/dataset/large_file.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = []


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 2
    ns = runtime.TemplateNamespace('__anon_0xaa94db4cL', context._clean_inheritance_tokens(), templateuri=u'/dataset/display.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0xaa94db4cL')] = ns

def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, u'/base.mako', _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xaa94db4cL')._populate(_import_ns, [u'render_deleted_data_message'])
        truncated_data = _import_ns.get('truncated_data', context.get('truncated_data', UNDEFINED))
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        util = _import_ns.get('util', context.get('util', UNDEFINED))
        render_deleted_data_message = _import_ns.get('render_deleted_data_message', context.get('render_deleted_data_message', UNDEFINED))
        trans = _import_ns.get('trans', context.get('trans', UNDEFINED))
        data = _import_ns.get('data', context.get('data', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 1
        __M_writer(u'\n')
        # SOURCE LINE 2
        __M_writer(u'\n\n')
        # SOURCE LINE 4
        __M_writer(unicode( render_deleted_data_message( data ) ))
        __M_writer(u'\n\n<div class="warningmessagelarge">\n    This dataset is large and only the first megabyte is shown below.<br />\n    <a href="')
        # SOURCE LINE 8
        __M_writer(unicode(h.url_for( controller='dataset', action='display', dataset_id=trans.security.encode_id( data.id ), filename='' )))
        __M_writer(u'">Show all</a> |\n    <a href="')
        # SOURCE LINE 9
        __M_writer(unicode(h.url_for( controller='dataset', action='display', dataset_id=trans.security.encode_id( data.id ), to_ext=data.ext )))
        __M_writer(u'">Save</a>\n</div>\n\n<pre>\n')
        # SOURCE LINE 13
        __M_writer(filters.html_escape(unicode( util.unicodify( truncated_data ) )))
        __M_writer(u'\n</pre>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


