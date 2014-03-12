# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392805205.664174
_template_filename=u'templates/webapps/galaxy/history/history_panel.mako'
_template_uri=u'/history/history_panel.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['bootstrapped_history_panel', 'history_panel', 'current_history_panel', 'history_panel_javascripts']


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 1
    ns = runtime.TemplateNamespace('__anon_0x29af12d0', context._clean_inheritance_tokens(), templateuri=u'/utils/localization.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0x29af12d0')] = ns

def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x29af12d0')._populate(_import_ns, [u'localize_js_strings'])
        __M_writer = context.writer()
        __M_writer(u'\n\n')
        # SOURCE LINE 29
        __M_writer(u'\n\n\n')
        # SOURCE LINE 55
        __M_writer(u'\n\n\n')
        # SOURCE LINE 87
        __M_writer(u'\n\n\n')
        # SOURCE LINE 158
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_bootstrapped_history_panel(context,history,hdas,selector_to_attach_to=None,show_deleted=None,show_hidden=None,hda_id=None):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x29af12d0')._populate(_import_ns, [u'localize_js_strings'])
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        def history_panel_javascripts():
            return render_history_panel_javascripts(context)
        __M_writer = context.writer()
        # SOURCE LINE 60
        __M_writer(u'\n\n')
        # SOURCE LINE 62
        __M_writer(unicode(history_panel_javascripts()))
        __M_writer(u'\n\n<script type="text/javascript">\nonhistoryready.done( function( historyPanel ){\n    // attach a panel to selector_to_attach_to and use a history model with bootstrapped data\n\n    // history module is already in the dpn chain from the panel. We can re-scope it here.\n    var historyModel = require( \'mvc/history/history-model\' ),\n        debugging = JSON.parse( sessionStorage.getItem( \'debugging\' ) ) || false,\n        historyJSON = ')
        # SOURCE LINE 71
        __M_writer(unicode(h.to_json_string( history )))
        __M_writer(u',\n        hdaJSON = ')
        # SOURCE LINE 72
        __M_writer(unicode(h.to_json_string( hdas )))
        __M_writer(u';\n\n    var history = new historyModel.History( historyJSON, hdaJSON, {\n        logger: ( debugging )?( console ):( null )\n    });\n\n    var panel = new historyPanel.HistoryPanel({\n        show_deleted    : ')
        # SOURCE LINE 79
        __M_writer(unicode( 'true' if show_deleted == True else ( 'null' if show_deleted == None else 'false' ) ))
        __M_writer(u',\n        show_hidden     : ')
        # SOURCE LINE 80
        __M_writer(unicode( 'true' if show_hidden  == True else ( 'null' if show_hidden  == None else 'false' ) ))
        __M_writer(u',\n        el              : $( "')
        # SOURCE LINE 81
        __M_writer(unicode(selector_to_attach_to))
        __M_writer(u'" ),\n        model           : history,\n        onready         : function(){ this.render(); }\n    });\n})\n</script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_history_panel(context,history_id,selector_to_attach_to=None,show_deleted=None,show_hidden=None,hda_id=None):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x29af12d0')._populate(_import_ns, [u'localize_js_strings'])
        def history_panel_javascripts():
            return render_history_panel_javascripts(context)
        __M_writer = context.writer()
        # SOURCE LINE 34
        __M_writer(u'\n\n')
        # SOURCE LINE 36
        __M_writer(unicode(history_panel_javascripts()))
        __M_writer(u'\n\n<script type="text/javascript">\nonhistoryready.done( function( historyPanel ){\n    // attach a panel to selector_to_attach_to and load the history/hdas with the given history_id over the api\n    var panel = new historyPanel.HistoryPanel({\n        show_deleted    : ')
        # SOURCE LINE 42
        __M_writer(unicode( 'true' if show_deleted == True else ( 'null' if show_deleted == None else 'false' ) ))
        __M_writer(u',\n        show_hidden     : ')
        # SOURCE LINE 43
        __M_writer(unicode( 'true' if show_hidden  == True else ( 'null' if show_hidden  == None else 'false' ) ))
        __M_writer(u',\n        el              : $( "')
        # SOURCE LINE 44
        __M_writer(unicode(selector_to_attach_to))
        __M_writer(u'" ),\n        onready         : function loadHistoryById(){\n            var panel = this;\n            this.loadHistoryWithHDADetails( \'')
        # SOURCE LINE 47
        __M_writer(unicode(history_id))
        __M_writer(u"' )\n                .fail( function(){\n                    panel.render();\n                });\n            }\n    });\n});\n</script>\n")
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_current_history_panel(context,selector_to_attach_to=None,show_deleted=None,show_hidden=None,hda_id=None):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x29af12d0')._populate(_import_ns, [u'localize_js_strings'])
        def history_panel_javascripts():
            return render_history_panel_javascripts(context)
        __M_writer = context.writer()
        # SOURCE LINE 4
        __M_writer(u'\n\n')
        # SOURCE LINE 6
        __M_writer(unicode(history_panel_javascripts()))
        __M_writer(u'\n\n<script type="text/javascript">\nonhistoryready.done( function( historyPanel ){\n    // attach a panel to selector_to_attach_to and load the current history/hdas over the api\n    var currPanel = new historyPanel.HistoryPanel({\n        // is page sending in show settings? if so override history\'s\n        show_deleted    : ')
        # SOURCE LINE 13
        __M_writer(unicode( 'true' if show_deleted == True else ( 'null' if show_deleted == None else 'false' ) ))
        __M_writer(u',\n        show_hidden     : ')
        # SOURCE LINE 14
        __M_writer(unicode( 'true' if show_hidden  == True else ( 'null' if show_hidden  == None else 'false' ) ))
        __M_writer(u',\n        el              : $( "')
        # SOURCE LINE 15
        __M_writer(unicode(selector_to_attach_to))
        __M_writer(u'" ),\n        linkTarget      : \'galaxy_main\',\n        onready         : function loadAsCurrentHistoryPanel(){\n            var panel = this;\n            this.connectToQuotaMeter( Galaxy.quotaMeter ).connectToOptionsMenu( Galaxy.historyOptionsMenu );\n            this.loadCurrentHistory()\n                .fail( function(){\n                    panel.render();\n                });\n            }\n    });\n    Galaxy.currHistoryPanel = currPanel;\n});\n</script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_history_panel_javascripts(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0x29af12d0')._populate(_import_ns, [u'localize_js_strings'])
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        localize_js_strings = _import_ns.get('localize_js_strings', context.get('localize_js_strings', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 91
        __M_writer(u'\n')
        # SOURCE LINE 92
        __M_writer(unicode(h.js(
    "utils/localization",
    "mvc/base-mvc",
    "mvc/tags",
    "mvc/annotations"
)))
        # SOURCE LINE 97
        __M_writer(u'\n\n')
        # SOURCE LINE 100
        __M_writer(unicode(h.templates(
    "history-templates",
    "helpers-common-templates"
)))
        # SOURCE LINE 103
        __M_writer(u'\n\n')
        # SOURCE LINE 105
        __M_writer(unicode(localize_js_strings([
    # not needed?: "Galaxy History",
    'refresh',
    'collapse all',
    'hide deleted',
    'hide hidden',
    'You are currently viewing a deleted history!',
    "Your history is empty. Click 'Get Data' on the left pane to start",

    # from history_common.mako
    'Download',
    'Display Data',
    'View data',
    'Edit attributes',
    'Delete',
    'Job is waiting to run',
    'View Details',
    'Job is currently running',
    #'Run this job again',
    'Metadata is being Auto-Detected.',
    'No data: ',
    'format: ',
    'database: ',
    #TODO localized data.dbkey??
    'Info: ',
    #TODO localized display_app.display_name??
    # _( link_app.name )
    # localized peek...ugh
    'Error: unknown dataset state'
])))
        # SOURCE LINE 134
        __M_writer(u'\n\n<script type="text/javascript">\nvar debugging = JSON.parse( sessionStorage.getItem( \'debugging\' ) ) || false,\n    // use deferred to allow multiple callbacks (.done())\n    onhistoryready = jQuery.Deferred();\n\nrequire.config({\n    baseUrl : "')
        # SOURCE LINE 142
        __M_writer(unicode(h.url_for( '/static/scripts' )))
        __M_writer(u'"\n});\n\n// requirejs optimizer:\n   //r.js -o baseUrl=\'/Users/carleberhard/galaxy/iframe-2-hpanel/static/scripts\' ')
        # SOURCE LINE 147
        __M_writer(u'   //        name=./mvc/history/history-panel.js out=history-panel.min.js\n//TODO: can\'t get either to work - historyPanel is undefined\n//require([ "history-panel.min" ], function( historyPanel ){\n//require([ "/static/scripts/history-panel.min.js" ], function( historyPanel ){\n\nrequire([ "mvc/history/history-panel" ], function( historyPanel ){\n    $(function(){\n        onhistoryready.resolve( historyPanel )\n    });\n});\n</script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


