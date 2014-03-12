# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392818457.873492
_template_filename=u'templates/tagging_common.mako'
_template_uri=u'/tagging_common.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['render_tagging_element_html', 'render_tool_tagging_elements', 'render_individual_tagging_element', 'render_community_tagging_element']


# SOURCE LINE 1
 
from cgi import escape 
from galaxy.web.framework.helpers import iff
from random import random
from sys import maxint
from math import floor
from galaxy.model import Tag, ItemTagAssociation


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        in_form = context.get('in_form', UNDEFINED)
        def render_community_tagging_element(tagged_item=None,elt_context=None,use_toggle_link=False,tag_click_fn='default_tag_click_fn'):
            return render_render_community_tagging_element(context.locals_(__M_locals),tagged_item,elt_context,use_toggle_link,tag_click_fn)
        elt_context = context.get('elt_context', UNDEFINED)
        use_toggle_link = context.get('use_toggle_link', UNDEFINED)
        user = context.get('user', UNDEFINED)
        def render_individual_tagging_element(user=None,tagged_item=None,elt_context=None,use_toggle_link=True,in_form=False,input_size='15',tag_click_fn='default_tag_click_fn',get_toggle_link_text_fn='default_get_toggle_link_text_fn',editable=True,render_add_tag_button=True):
            return render_render_individual_tagging_element(context.locals_(__M_locals),user,tagged_item,elt_context,use_toggle_link,in_form,input_size,tag_click_fn,get_toggle_link_text_fn,editable,render_add_tag_button)
        tag_click_fn = context.get('tag_click_fn', UNDEFINED)
        input_size = context.get('input_size', UNDEFINED)
        tagged_item = context.get('tagged_item', UNDEFINED)
        tag_type = context.get('tag_type', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 8
        __M_writer(u'\n\n')
        # SOURCE LINE 11
        if tagged_item is not None:
            # SOURCE LINE 12
            if tag_type == "individual":
                # SOURCE LINE 13
                __M_writer(u'        ')
                __M_writer(unicode(render_individual_tagging_element( user=user, tagged_item=tagged_item, elt_context=elt_context, in_form=in_form, input_size=input_size, tag_click_fn=tag_click_fn, use_toggle_link=use_toggle_link )))
                __M_writer(u'\n')
                # SOURCE LINE 14
            elif tag_type == "community":
                # SOURCE LINE 15
                __M_writer(u'        ')
                __M_writer(unicode(render_community_tagging_element(tagged_item=tagged_item, elt_context=elt_context, tag_click_fn=tag_click_fn)))
                __M_writer(u'\n')
                pass
            pass
        # SOURCE LINE 18
        __M_writer(u'\n')
        # SOURCE LINE 85
        __M_writer(u'\n\n')
        # SOURCE LINE 100
        __M_writer(u'\n\n')
        # SOURCE LINE 118
        __M_writer(u'\n\n\n')
        # SOURCE LINE 231
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_tagging_element_html(context,elt_id=None,tags=None,editable=True,use_toggle_link=True,input_size='15',in_form=False,tag_type='individual',render_add_tag_button=True):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        unicode = context.get('unicode', UNDEFINED)
        isinstance = context.get('isinstance', UNDEFINED)
        len = context.get('len', UNDEFINED)
        str = context.get('str', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 20
        __M_writer(u'\n')
        # SOURCE LINE 22
        __M_writer(u'    ')
 
        num_tags = len( tags )
            
        
        # SOURCE LINE 24
        __M_writer(u'\n    <div class="tag-element"\n')
        # SOURCE LINE 26
        if elt_id:
            # SOURCE LINE 27
            __M_writer(u'            id="')
            __M_writer(unicode(elt_id))
            __M_writer(u'"\n')
            pass
        # SOURCE LINE 30
        if num_tags == 0 and not editable:
            # SOURCE LINE 31
            __M_writer(u'            style="display: none"\n')
            pass
        # SOURCE LINE 33
        __M_writer(u'    >\n')
        # SOURCE LINE 34
        if use_toggle_link:
            # SOURCE LINE 35
            __M_writer(u'            <a class="toggle-link" href="#">')
            __M_writer(unicode(num_tags))
            __M_writer(u' Tag')
            __M_writer(unicode(iff( num_tags == 1, "", "s")))
            __M_writer(u'</a>\n')
            pass
        # SOURCE LINE 37
        __M_writer(u'        <div class="tag-area \n')
        # SOURCE LINE 38
        if tag_type == 'individual':
            # SOURCE LINE 39
            __M_writer(u'                individual-tag-area\n')
            pass
        # SOURCE LINE 41
        __M_writer(u'        ">\n\n')
        # SOURCE LINE 44
        for tag in tags:
            # SOURCE LINE 45
            __M_writer(u'                ')

                    ## Handle both Tag and ItemTagAssociation objects.
            if isinstance( tag, Tag ):
                tag_name = tag.name
                tag_value = None
            elif isinstance( tag, ItemTagAssociation ):
                tag_name = tag.user_tname
                tag_value = tag.user_value
            ## Convert tag name, value to unicode.
            if isinstance( tag_name, str ):
                tag_name = unicode( escape( tag_name ), 'utf-8' )
                if tag_value:
                    tag_value = unicode( escape( tag_value ), 'utf-8' )
            if tag_value:
                tag_str = tag_name + ":" + tag_value
            else:
                tag_str = tag_name
                            
            
            # SOURCE LINE 62
            __M_writer(u'\n                <span class="tag-button">\n                    <span class="tag-name">')
            # SOURCE LINE 64
            __M_writer(unicode(tag_str))
            __M_writer(u'</span>\n')
            # SOURCE LINE 65
            if editable:
                # SOURCE LINE 66
                __M_writer(u'                        <img class="delete-tag-img" src="')
                __M_writer(unicode(h.url_for('/static/images/delete_tag_icon_gray.png')))
                __M_writer(u'"/>\n')
                pass
            # SOURCE LINE 68
            __M_writer(u'                </span>\n')
            pass
        # SOURCE LINE 70
        __M_writer(u'            \n')
        # SOURCE LINE 72
        if editable:
            # SOURCE LINE 73
            if in_form:
                # SOURCE LINE 74
                __M_writer(u'                    <textarea class="tag-input" rows=\'1\' cols=\'')
                __M_writer(unicode(input_size))
                __M_writer(u"'></textarea>\n")
                # SOURCE LINE 75
            else:
                # SOURCE LINE 76
                __M_writer(u'                    <input class="tag-input" type=\'text\' size=\'')
                __M_writer(unicode(input_size))
                __M_writer(u"'/>\n")
                pass
            # SOURCE LINE 79
            if render_add_tag_button:
                # SOURCE LINE 80
                __M_writer(u"                    <img src='")
                __M_writer(unicode(h.url_for('/static/images/fugue/tag--plus.png')))
                __M_writer(u'\' class="add-tag-button" title="Add tags"/>\n')
                pass
            pass
        # SOURCE LINE 83
        __M_writer(u'        </div>\n    </div>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_tool_tagging_elements(context):
    context.caller_stack._push_frame()
    try:
        int = context.get('int', UNDEFINED)
        self = context.get('self', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 88
        __M_writer(u'\n    ')
        # SOURCE LINE 89

        elt_id = int ( floor ( random()*maxint ) )
        tags = trans.app.tag_handler.get_tool_tags( trans )
            
        
        # SOURCE LINE 92
        __M_writer(u'\n    ')
        # SOURCE LINE 93
        __M_writer(unicode(self.render_tagging_element_html(elt_id=elt_id, \
                                        tags=tags, \
                                        editable=False, \
                                        use_toggle_link=False )))
        # SOURCE LINE 96
        __M_writer(u'\n    <script type="text/javascript">\n        init_tag_click_function($(\'#')
        # SOURCE LINE 98
        __M_writer(unicode(elt_id))
        __M_writer(u"'), tool_tag_click);\n    </script>\n")
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_individual_tagging_element(context,user=None,tagged_item=None,elt_context=None,use_toggle_link=True,in_form=False,input_size='15',tag_click_fn='default_tag_click_fn',get_toggle_link_text_fn='default_get_toggle_link_text_fn',editable=True,render_add_tag_button=True):
    context.caller_stack._push_frame()
    try:
        isinstance = context.get('isinstance', UNDEFINED)
        unicode = context.get('unicode', UNDEFINED)
        int = context.get('int', UNDEFINED)
        h = context.get('h', UNDEFINED)
        self = context.get('self', UNDEFINED)
        dict = context.get('dict', UNDEFINED)
        str = context.get('str', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 122
        __M_writer(u'\n')
        # SOURCE LINE 124
        __M_writer(u'    ')

        # Useful ids.
        tagged_item_id = str( trans.security.encode_id ( tagged_item.id ) )
        elt_id = int ( floor ( random()*maxint ) )
        
        # Get list of user's item tags. TODO: implement owner_tags for all taggable objects and use here.
        item_tags = [ tag for tag in tagged_item.tags if ( tag.user == user ) ]
            
        
        # SOURCE LINE 131
        __M_writer(u'\n    \n')
        # SOURCE LINE 134
        __M_writer(u'    ')
        __M_writer(unicode(self.render_tagging_element_html(elt_id=elt_id, tags=item_tags, editable=editable, use_toggle_link=use_toggle_link, input_size=input_size, in_form=in_form, render_add_tag_button=render_add_tag_button)))
        __M_writer(u'\n    \n')
        # SOURCE LINE 137
        __M_writer(u'    <script type="text/javascript">\n        //\n        // Set up autocomplete tagger.\n        //\n\n        //\n        // Default function get text to display on the toggle link.\n        //\n        var default_get_toggle_link_text_fn = function(tags)\n        {\n            var text = "";\n            var num_tags = obj_length(tags);\n            if (num_tags != 0)\n              {\n                text = num_tags + (num_tags != 1 ? " Tags" : " Tag");\n                /*\n                // Show first N tags; hide the rest.\n                var max_to_show = 1;\n    \n                // Build tag string.\n                var tag_strs = new Array();\n                var count = 0;\n                for (tag_name in tags)\n                  {\n                    tag_value = tags[tag_name];\n                    tag_strs[tag_strs.length] = build_tag_str(tag_name, tag_value);\n                    if (++count == max_to_show)\n                      break;\n                  }\n                tag_str = tag_strs.join(", ");\n            \n                // Finalize text.\n                var num_tags_hiding = num_tags - max_to_show;\n                text = "Tags: " + tag_str + \n                  (num_tags_hiding != 0 ? " and " + num_tags_hiding + " more" : "");\n                */\n              }\n            else\n              {\n                // No tags.\n                text = "Add tags";\n              }\n            return text;\n        };\n        \n        // Default function to handle a tag click.\n        var default_tag_click_fn = function(tag_name, tag_value) { };\n        \n        ')
        # SOURCE LINE 185

            ## Build dict of tag name, values.
        tag_names_and_values = dict()
        for tag in item_tags:
            tag_name = tag.user_tname
            tag_value = ""
            if tag.value is not None:
                tag_value = tag.user_value
            ## Tag names and values may be string or unicode object.
            if isinstance( tag_name, str ):
                tag_names_and_values[unicode(tag_name, 'utf-8')] = unicode(tag_value, 'utf-8')
            else: ## isInstance( tag_name, unicode ):
                tag_names_and_values[tag_name] = tag_value
                
        
        # SOURCE LINE 198
        __M_writer(u'\n        var options =\n        {\n            tags : ')
        # SOURCE LINE 201
        __M_writer(unicode(h.to_json_string(tag_names_and_values)))
        __M_writer(u',\n            editable : ')
        # SOURCE LINE 202
        __M_writer(unicode(iff( editable, 'true', 'false' )))
        __M_writer(u',\n            get_toggle_link_text_fn: ')
        # SOURCE LINE 203
        __M_writer(unicode(get_toggle_link_text_fn))
        __M_writer(u',\n            tag_click_fn: ')
        # SOURCE LINE 204
        __M_writer(unicode(tag_click_fn))
        __M_writer(u',\n')
        # SOURCE LINE 206
        __M_writer(u'            ajax_autocomplete_tag_url: "')
        __M_writer(unicode(h.url_for( controller='/tag', action='tag_autocomplete_data', item_id=tagged_item_id, item_class=tagged_item.__class__.__name__ )))
        __M_writer(u'",\n            ajax_add_tag_url: "')
        # SOURCE LINE 207
        __M_writer(unicode(h.url_for( controller='/tag', action='add_tag_async', item_id=tagged_item_id, item_class=tagged_item.__class__.__name__, context=elt_context )))
        __M_writer(u'",\n            ajax_delete_tag_url: "')
        # SOURCE LINE 208
        __M_writer(unicode(h.url_for( controller='/tag', action='remove_tag_async', item_id=tagged_item_id, item_class=tagged_item.__class__.__name__, context=elt_context )))
        __M_writer(u'",\n            delete_tag_img: "')
        # SOURCE LINE 209
        __M_writer(unicode(h.url_for('/static/images/delete_tag_icon_gray.png')))
        __M_writer(u'",\n            delete_tag_img_rollover: "')
        # SOURCE LINE 210
        __M_writer(unicode(h.url_for('/static/images/delete_tag_icon_white.png')))
        __M_writer(u'",\n            use_toggle_link: ')
        # SOURCE LINE 211
        __M_writer(unicode(iff( use_toggle_link, 'true', 'false' )))
        __M_writer(u"\n         };\n         \n        $('#")
        # SOURCE LINE 214
        __M_writer(unicode(elt_id))
        __M_writer(u"').autocomplete_tagging(options);\n    </script>\n    \n")
        # SOURCE LINE 218
        __M_writer(u'    <style>\n    .tag-area {\n        display: ')
        # SOURCE LINE 220
        __M_writer(unicode(iff( use_toggle_link, "none", "block" )))
        __M_writer(u';\n    }\n    </style>\n\n    <noscript>\n    <style>\n    .tag-area {\n        display: block;\n    }\n    </style>\n    </noscript>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_community_tagging_element(context,tagged_item=None,elt_context=None,use_toggle_link=False,tag_click_fn='default_tag_click_fn'):
    context.caller_stack._push_frame()
    try:
        int = context.get('int', UNDEFINED)
        self = context.get('self', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 103
        __M_writer(u'\n')
        # SOURCE LINE 105
        __M_writer(u'    ')
 
        elt_id = int ( floor ( random()*maxint ) ) 
        community_tags = trans.app.tag_handler.get_community_tags( trans, item=tagged_item, limit=5 )
            
        
        # SOURCE LINE 108
        __M_writer(u'\n    ')
        # SOURCE LINE 109
        __M_writer(unicode(self.render_tagging_element_html(elt_id=elt_id, \
                                        tags=community_tags, \
                                        use_toggle_link=use_toggle_link, \
                                        editable=False, tag_type="community")))
        # SOURCE LINE 112
        __M_writer(u'\n    \n')
        # SOURCE LINE 115
        __M_writer(u'    <script type="text/javascript">\n        init_tag_click_function($(\'#')
        # SOURCE LINE 116
        __M_writer(unicode(elt_id))
        __M_writer(u"'), ")
        __M_writer(unicode(tag_click_fn))
        __M_writer(u');\n    </script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


