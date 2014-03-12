# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392818457.860417
_template_filename=u'templates/display_common.mako'
_template_uri=u'/display_common.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['get_class_plural_display_name', 'get_item_user', 'get_item_plural', 'render_message', 'get_class_plural', 'get_item_name', 'get_class_display_name', 'get_controller_name', 'get_item_slug', 'get_history_link']


# SOURCE LINE 8
from galaxy import model 

def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        __M_writer = context.writer()
        # SOURCE LINE 7
        __M_writer(u'\n')
        # SOURCE LINE 8
        __M_writer(u'\n\n')
        # SOURCE LINE 19
        __M_writer(u'\n\n')
        # SOURCE LINE 38
        __M_writer(u'\n\n')
        # SOURCE LINE 51
        __M_writer(u'\n\n')
        # SOURCE LINE 64
        __M_writer(u'\n\n')
        # SOURCE LINE 69
        __M_writer(u'\n\n')
        # SOURCE LINE 96
        __M_writer(u'\n\n')
        # SOURCE LINE 112
        __M_writer(u'\n\n')
        # SOURCE LINE 123
        __M_writer(u'\n\n')
        # SOURCE LINE 134
        __M_writer(u'\n\n')
        # SOURCE LINE 143
        __M_writer(u'\n\n')
        # SOURCE LINE 153
        __M_writer(u'\n\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_class_plural_display_name(context,a_class):
    context.caller_stack._push_frame()
    try:
        def get_class_display_name(a_class):
            return render_get_class_display_name(context,a_class)
        __M_writer = context.writer()
        # SOURCE LINE 41
        __M_writer(u'\n')
        # SOURCE LINE 42

    # Start with exceptions, end with default.
        if a_class is model.History:
            return "Histories"
        elif a_class is model.FormDefinitionCurrent:
            return "Forms"
        else:
            return get_class_display_name( a_class ) + "s"
        
        
        # SOURCE LINE 50
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_item_user(context,item):
    context.caller_stack._push_frame()
    try:
        isinstance = context.get('isinstance', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 115
        __M_writer(u'\n    ')
        # SOURCE LINE 116

        # Exceptions first, default last.
        if isinstance( item, model.HistoryDatasetAssociation ):
            return item.history.user
        else:
            return item.user
            
        
        # SOURCE LINE 122
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_item_plural(context,item):
    context.caller_stack._push_frame()
    try:
        def get_class_plural(a_class):
            return render_get_class_plural(context,a_class)
        __M_writer = context.writer()
        # SOURCE LINE 67
        __M_writer(u'\n    ')
        # SOURCE LINE 68
        return get_class_plural( item.__class__ ) 
        
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_message(context,message,status):
    context.caller_stack._push_frame()
    try:
        util = context.get('util', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 146
        __M_writer(u'\n')
        # SOURCE LINE 147
        if message:
            # SOURCE LINE 148
            __M_writer(u'        <p>\n            <div class="')
            # SOURCE LINE 149
            __M_writer(unicode(status))
            __M_writer(u'message transient-message">')
            __M_writer(unicode(util.restore_text( message )))
            __M_writer(u'</div>\n            <div style="clear: both"></div>\n        </p>\n')
            pass
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_class_plural(context,a_class):
    context.caller_stack._push_frame()
    try:
        __M_writer = context.writer()
        # SOURCE LINE 72
        __M_writer(u'\n')
        # SOURCE LINE 73

        if a_class == model.History:
            class_plural = "Histories"
        elif a_class == model.StoredWorkflow:
            class_plural = "Workflows"
        elif a_class == model.Page:
            class_plural = "Pages"
        elif a_class == model.Library:
            class_plural = "Libraries"
        elif a_class == model.HistoryDatasetAssociation:
            class_plural = "Datasets"
        elif a_class == model.SampleDataset:
            class_plural = "Sample Datasets"
        elif a_class == model.FormDefinitionCurrent:
            class_plural = "Forms"
        elif a_class == model.RequestType:
            class_plural = "request types"
        elif a_class == model.UserOpenID:
            class_plural = "OpenIDs"
        else:
            class_plural = "items"
        return class_plural
        
        
        # SOURCE LINE 95
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_item_name(context,item):
    context.caller_stack._push_frame()
    try:
        hasattr = context.get('hasattr', UNDEFINED)
        type = context.get('type', UNDEFINED)
        unicode = context.get('unicode', UNDEFINED)
        str = context.get('str', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 21
        __M_writer(u'\n    ')
        # SOURCE LINE 22
 
        # Start with exceptions, end with default.
        if type( item ) is model.Page:
            item_name = item.title
        elif type( item ) is model.Visualization:
            item_name = item.title
        elif hasattr( item, 'get_display_name'):
            item_name = item.get_display_name()
        else:
            item_name = item.name
        
        # Encode in unicode.
        if type( item_name ) is str:
            item_name = unicode( item_name, 'utf-8' )
        return item_name    
            
        
        # SOURCE LINE 37
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_class_display_name(context,a_class):
    context.caller_stack._push_frame()
    try:
        __M_writer = context.writer()
        # SOURCE LINE 54
        __M_writer(u'\n')
        # SOURCE LINE 55

    ## Start with exceptions, end with default.
        if a_class is model.StoredWorkflow:
            return "Workflow"
        elif a_class is model.HistoryDatasetAssociation:
            return "Dataset"
        else:
            return a_class.__name__
        
        
        # SOURCE LINE 63
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_controller_name(context,item):
    context.caller_stack._push_frame()
    try:
        isinstance = context.get('isinstance', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 99
        __M_writer(u'\n    ')
        # SOURCE LINE 100

        if isinstance( item, model.History ):
            return "history"
        elif isinstance( item, model.StoredWorkflow ):
            return "workflow"
        elif isinstance( item, model.HistoryDatasetAssociation ):
            return "dataset"
        elif isinstance( item, model.Page ):
            return "page"
        elif isinstance( item, model.Visualization ):
            return "visualization"
            
        
        # SOURCE LINE 111
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_item_slug(context,item):
    context.caller_stack._push_frame()
    try:
        trans = context.get('trans', UNDEFINED)
        isinstance = context.get('isinstance', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 126
        __M_writer(u'\n    ')
        # SOURCE LINE 127

        # Exceptions first, default last.
        if isinstance( item, model.HistoryDatasetAssociation ):
            return trans.security.encode_id( item.id )
        else:
            return item.slug
            
        
        # SOURCE LINE 133
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_history_link(context,history,qualify=False):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 137
        __M_writer(u'\n')
        # SOURCE LINE 138
        if history.slug and history.user.username:
            # SOURCE LINE 139
            __M_writer(u'        ')
            return h.url_for( controller='/history', action='display_by_username_and_slug', username=history.user.username, slug=history.slug, qualified=qualify ) 
            
            __M_writer(u'\n')
            # SOURCE LINE 140
        else:
            # SOURCE LINE 141
            __M_writer(u'        ')
            return h.url_for( controller='/history', action='view', id=trans.security.encode_id( history.id ), qualified=qualify, use_panels=context.get('use_panels', True) ) 
            
            __M_writer(u'\n')
            pass
        return ''
    finally:
        context.caller_stack._pop_frame()


