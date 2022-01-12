#!/usr/bin/env python
# coding: utf-8

# In[112]:


#!/usr/bin/env python3
import sys
import argparse
import csv
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By

def wait_to_load(driver, class_name, timeout):
    try:
        loaded = EC.presence_of_element_located((By.CLASS_NAME, class_name))
        WebDriverWait(driver, timeout).until(loaded)
    except TimeoutException:
        print("Waited for longer than " + str(timeout) + " seconds to load.")
        
def wait_until_clickable(driver, class_name, timeout):
    try:
        wait_to_load(driver, class_name, timeout)
        clickable = EC.element_to_be_clickable((By.CLASS_NAME, class_name))
        WebDriverWait(driver, timeout).until(clickable)
    except TimeoutException:
        print("Waited for longer than " + str(timeout) + " seconds to load.")
    
def main(url, csv_file):
    
    driver = webdriver.Chrome('./chromedriver')
    #driver.get('https://www.yelp.com/search?cflt=arts&find_loc=Boston')
    driver.get(url)
    html_soup = BeautifulSoup(driver.page_source, 'html.parser')
    total_pages_parent = html_soup.find(class_ = 'border-color--default__09f24__NPAKY text-align--center__09f24__fYBGO')
    total_pages_child = total_pages_parent.findChild(class_ = 'css-1e4fdj9')
    total_pages = int(total_pages_child.string.split()[-1])
    
    file = open(csv_file, 'w')
    writer = csv.writer(file)
    writer.writerow(['name', 'rating', 'hours', 'categories', 'address'])
    
    for i in range(total_pages - 1):
        print('starting outer loop')
        # go thru the page and gather data
        
        wait_to_load(driver, 'css-uvzfg9', 10)
        wait_until_clickable(driver, 'css-uvzfg9', 10)
    
        elements = driver.find_elements_by_class_name('css-uvzfg9')
    
        for j in range(len(elements)):
            wait_until_clickable(driver, 'css-uvzfg9', 10)
            try:
                element = driver.find_elements_by_class_name('css-uvzfg9')[j]
            except:
                continue
            element.click()
            wait_to_load(driver, 'css-1x9iesk', 10)
            # get data
            html_soup = BeautifulSoup(driver.page_source, 'html.parser')
            try:
                name = html_soup.find(class_ = 'css-1x9iesk').string
            except:
                name = 'N/A'
            print(name)
            try:
                rating = html_soup.find(class_ = 'i-stars__09f24__foihJ')['aria-label']
            except:
                rating = 'N/A'
            print(rating)
            try:
                hours = html_soup.findAll(class_ = 'no-wrap__09f24__c3plq css-1h7ysrc')
                hours = list(map(lambda hour: hour.string, hours))
                hours = ';'.join(hours)
            except:
                hours = 'N/A'
            print(hours)
            categories = html_soup.findAll(class_ = 'css-1yy09vp')[1::]
            category_list = [ ]
            for category in categories:
                try:
                    category_child = category.findChild(class_ = 'css-1422juy').string
                    category_list.append(category_child)
                except:
                    pass
            category_list = ';'.join(category_list)
            print(category_list)
            try:
                address = html_soup.find('address')
                address_strings = address.findChildren(class_ = 'raw__09f24__T4Ezm')
                address = address_strings[0].string + ', ' + address_strings[1].string
            except:
                address = 'N/A'
            print(address)
            writer.writerow([name, rating, hours, category_list, address])
            driver.back()
    
        wait_to_load(driver, 'icon--24-chevron-right-v2 navigation-button-icon__09f24__Bmrde css-1kq79li', 10)
    
        next_btn = driver.find_element_by_class_name('next-link')
        next_btn.click()
        print('clicked next button')
        
    file.close()

if __name__ == "__main__":
    ### arg setup
    url = sys.argv[1]
    csv_file = sys.argv[2]
    print('url: ' + url + ' csv: ' + csv_file)
    main(url, csv_file)

    


# In[ ]:




